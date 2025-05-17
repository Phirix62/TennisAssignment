package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEmailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/test-email")
    public ResponseEntity<String> sendTestEmail() {
        mailService.sendRegistrationDecision(
                "your@email.com", // ← doesn’t need to exist
                "Test User",
                "Demo Tournament",
                "ACCEPTED"
        );
        return ResponseEntity.ok("Test email sent");
    }
}

