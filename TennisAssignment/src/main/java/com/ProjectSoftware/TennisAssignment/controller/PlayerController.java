package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.Match;
import com.ProjectSoftware.TennisAssignment.entity.Referee;
import com.ProjectSoftware.TennisAssignment.entity.TennisPlayer;
import com.ProjectSoftware.TennisAssignment.entity.Tournament;
import com.ProjectSoftware.TennisAssignment.entity.TournamentRegistration;
import com.ProjectSoftware.TennisAssignment.repository.MatchRepository;
import com.ProjectSoftware.TennisAssignment.repository.TournamentRepository;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import com.ProjectSoftware.TennisAssignment.repository.TournamentRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private MatchRepository matchRepo;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TournamentRegistrationRepository registrationRepo;

    // Player joins a tournament
    @PostMapping("/{username}/join/{tournamentId}")
    public String requestToJoinTournament(@PathVariable String username, @PathVariable Long tournamentId) {
        TennisPlayer player = (TennisPlayer) userRepo.findByUsername(username).orElseThrow();
        Tournament tournament = tournamentRepo.findById(tournamentId).orElseThrow();

        if (registrationRepo.findByTournamentAndPlayer(tournament, player).isPresent()) {
            return "You already requested to join this tournament.";
        }

        TournamentRegistration registration = new TournamentRegistration();
        registration.setPlayer(player);
        registration.setTournament(tournament);
        registration.setStatus("PENDING");
        registration.setRegisteredAt(LocalDateTime.now());

        registrationRepo.save(registration);

        return "Tournament registration submitted and is pending approval.";
    }

    // Player views his matches
    @GetMapping("/{username}/matches")
    public List<Match> getPlayerMatches(@PathVariable String username) {
        TennisPlayer player = (TennisPlayer) userRepo.findByUsername(username).orElseThrow();
        return matchRepo.findByPlayer1OrPlayer2(player, player);
    }
}

