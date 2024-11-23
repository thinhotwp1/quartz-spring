package telsoft.scheduler.quartz.manager.config.authentication;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import telsoft.scheduler.quartz.manager.entity.Decentralization;
import telsoft.scheduler.quartz.manager.entity.User;
import telsoft.scheduler.quartz.manager.repository.DecentralizationRepository;
import telsoft.scheduler.quartz.manager.repository.UserRepository;

import java.util.Optional;

@Configuration
public class InitAuthentication {
    public static final String ADMIN_ROLE = "admin";

    @Value("${init.admin.username}")
    private String userName;

    @Value("${init.admin.password}")
    private String password;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DecentralizationRepository decentralizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initAuthentication() {
        // Init admin role
        Optional<Decentralization> decentralizationOptional = decentralizationRepository.findById(ADMIN_ROLE);
        if (decentralizationOptional.isEmpty()) {
            Decentralization decentralization = Decentralization.builder()
                    .role(ADMIN_ROLE)
                    .access("*")
                    .build();
            decentralizationRepository.saveAndFlush(decentralization);
        }

        // Init admin user
        Optional<User> userOptional = userRepository.findById(userName);
        if (userOptional.isEmpty()) {
            User adminUser = User.builder()
                    .userName(userName)
                    .password(passwordEncoder.encode(password))
                    .role(ADMIN_ROLE)
                    .build();
            userRepository.saveAndFlush(adminUser);
        }
    }
}
