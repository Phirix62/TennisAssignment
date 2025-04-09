package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.User;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userRepo.findById(id);
    }

    // Update user
    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            userRepo.save(user);
            return "User updated.";
        } else {
            return "User not found.";
        }
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return "User deleted.";
        } else {
            return "User not found.";
        }
    }

    @Data
    static class UpdateUserRequest {
        private String username;
        private String email;
        private String fullName;
    }
}
