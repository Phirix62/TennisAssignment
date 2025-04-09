package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.*;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername()) || userRepo.existsByEmail(request.getEmail())) {
            return "Username or email already exists.";
        }

        User user;
        switch (request.getRole().toLowerCase()) {
            case "player" -> user = new TennisPlayer();
            case "referee" -> user = new Referee();
            case "admin" -> user = new Administrator();
            default -> throw new IllegalArgumentException("Invalid role");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepo.save(user);
        return "Registration successful.";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepo.findByUsername(request.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return "Login successful as " + user.getClass().getSimpleName();
            }
        }
        return "Invalid credentials.";
    }

    @Data
    static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private String fullName;
        private String role;
    }

    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }
}
