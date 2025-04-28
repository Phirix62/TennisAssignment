package com.ProjectSoftware.TennisAssignment.controller;


import com.ProjectSoftware.TennisAssignment.entity.User;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserRepository userRepo;

    @PutMapping("/{username}/update")
    public String updateAccount(@PathVariable String username, @RequestBody UpdateAccountRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());

        userRepo.save(user);
        return "Account updated successfully.";
    }

    @Data
    static class UpdateAccountRequest {
        private String username;
        private String email;
        private String fullName;
    }
}
