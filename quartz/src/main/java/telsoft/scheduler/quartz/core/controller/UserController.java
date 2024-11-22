package telsoft.scheduler.quartz.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telsoft.scheduler.quartz.core.dto.LoginRequest;
import telsoft.scheduler.quartz.core.dto.RegisterRequest;
import telsoft.scheduler.quartz.core.entity.Decentralization;
import telsoft.scheduler.quartz.core.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return userService.getAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest registerRequest) {
        userService.signup(registerRequest);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> removeUser(@RequestBody String userName) {
        userService.delete(userName);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/add-role")
    public ResponseEntity<?> addRole(@RequestBody Decentralization decentralization) {
        userService.addRole(decentralization);
        return ResponseEntity.ok("Success");
    }
}
