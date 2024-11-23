package telsoft.scheduler.quartz.manager.service;

import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import telsoft.scheduler.quartz.manager.config.authentication.InitAuthentication;
import telsoft.scheduler.quartz.manager.config.authentication.JwtTokenProvider;
import telsoft.scheduler.quartz.manager.dto.LoginRequest;
import telsoft.scheduler.quartz.manager.dto.LoginResponse;
import telsoft.scheduler.quartz.manager.dto.RegisterRequest;
import telsoft.scheduler.quartz.manager.entity.Decentralization;
import telsoft.scheduler.quartz.manager.entity.User;
import telsoft.scheduler.quartz.manager.exception.NotFoundException;
import telsoft.scheduler.quartz.manager.repository.DecentralizationRepository;
import telsoft.scheduler.quartz.manager.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DecentralizationRepository decentralizationRepository;

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();

        // Validate
        validateLoginRequest(loginRequest);

        UserDetails userDetails = org.springframework.security.core.userdetails.
                User.withUsername(loginRequest.getUser()).password(loginRequest.getPassword()).build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        loginResponse.setAccessToken(jwtTokenProvider.generateToken(usernamePasswordAuthenticationToken));

        return ResponseEntity.ok(loginResponse);
    }

    private void validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest.getUser().isEmpty() || loginRequest.getPassword().isEmpty()) {
            throw new NullPointerException("User, password is not null !");
        }

        // Get user from db
        Optional<User> user = userRepository.findById(loginRequest.getUser());
        if (user.isEmpty()) {
            throw new NotFoundException("Not found user: " + loginRequest.getUser());
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword()))
            throw new AccessDeniedException("User or password is wrong !");
    }

    public void signup(RegisterRequest request) {
        // Validate
        validateRequest(request);

        userRepository.saveAndFlush(
                User.builder()
                        .userName(request.getUserName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build());
    }

    private void validateRequest(RegisterRequest request) {
        // Validate params
        if (StringUtils.isEmpty(request.getUserName())
                || StringUtils.isEmpty(request.getPassword())
                || StringUtils.isEmpty(request.getRole())) {
            throw new NullPointerException("User, password, role can not null !");
        }

        // check admin user
        adminRoleCheck();

        // Check existed user
        Optional<User> user = userRepository.findById(request.getUserName());
        if (user.isPresent())
            throw new EntityExistsException("User existed");

        // Check role
        Optional<Decentralization> decentralizationOptional = findRole(request.getRole());
        if (decentralizationOptional.isEmpty())
            throw new NotFoundException("Not found role: " + request.getRole());
    }

    private void adminRoleCheck() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new AccessDeniedException("You are not admin");

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(InitAuthentication.ADMIN_ROLE)))
            throw new AccessDeniedException("You are not admin");
    }

    public void delete(String userName) {
        // Validate
        if (StringUtils.isEmpty(userName))
            throw new NullPointerException("Username is not null !");

        // check admin user
        adminRoleCheck();

        Optional<User> user = userRepository.findById(userName);
        if (user.isEmpty())
            throw new UsernameNotFoundException("Not found user name: " + userName);

        userRepository.delete(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username
        Optional<User> userOptional = userRepository.findById(username);

        if (userOptional.isEmpty()) throw new UsernameNotFoundException("Not found user: " + username);

        User user = userOptional.get();

        // Role list current:  admin
        List<String> listRole = List.of(user.getRole().toLowerCase().trim().split(","));

        // Get list role user
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : listRole) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),
                authorities);
    }

    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public void addRole(Decentralization decentralization) {
        decentralizationRepository.saveAndFlush(decentralization);
    }

    public Optional<Decentralization> findRole(String role) {
        return decentralizationRepository.findById(role);
    }

}