package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.TennisPlayer;
import com.ProjectSoftware.TennisAssignment.entity.Tournament;
import com.ProjectSoftware.TennisAssignment.entity.TournamentRegistration;
import com.ProjectSoftware.TennisAssignment.entity.User;
import com.ProjectSoftware.TennisAssignment.service.TournamentService;
import com.ProjectSoftware.TennisAssignment.service.MailService;
import com.ProjectSoftware.TennisAssignment.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TournamentRegistrationRepository registrationRepo;

    @Autowired
    private MatchRepository matchRepo;

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MailService mailService;

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

    @PutMapping("/registration/{id}/status")
    public ResponseEntity<String> updateRegistrationStatus(@PathVariable Long id, @RequestParam String status) {
    Optional<TournamentRegistration> optionalReg = registrationRepo.findById(id);
    if (optionalReg.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registration not found.");
    }

    TournamentRegistration reg = optionalReg.get();

    if (!status.equalsIgnoreCase("ACCEPTED") && !status.equalsIgnoreCase("REJECTED")) {
        return ResponseEntity.badRequest().body("Invalid status. Must be ACCEPTED or REJECTED.");
    }

    reg.setStatus(status.toUpperCase());
    registrationRepo.save(reg);

    if (status.equalsIgnoreCase("ACCEPTED")) {
        Tournament t = reg.getTournament();
        TennisPlayer p = reg.getPlayer();
        t.getPlayers().add(p);
        tournamentRepo.save(t);

        if (t.getPlayers().size() >= 4 && allRegistrationsAccepted(t)) {
            tournamentService.generateTournamentMatches(t);
        }
    }

    try {
    mailService.sendRegistrationDecision(
        reg.getPlayer().getEmail(),
        reg.getPlayer().getFullName(),
        reg.getTournament().getName(),
        status
    );
    } catch (Exception e) {
        e.printStackTrace(); // optional: log
        return ResponseEntity.status(HttpStatus.OK).body("Registration updated, but email could not be sent.");
    }

    return ResponseEntity.ok("Registration " + status.toUpperCase());
}

    private boolean allRegistrationsAccepted(Tournament tournament) {
        return registrationRepo.findByTournament(tournament).stream()
                .allMatch(r -> r.getStatus().equals("ACCEPTED"));
    }

    @GetMapping("/registrations")
    public List<TournamentRegistration> getAllRegistrations() {
        return registrationRepo.findAll();
    }

    @Data
    static class UpdateUserRequest {
        private String username;
        private String email;
        private String fullName;
    }
}
