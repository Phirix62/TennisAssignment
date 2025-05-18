package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.Match;
import com.ProjectSoftware.TennisAssignment.repository.MatchRepository;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import com.ProjectSoftware.TennisAssignment.repository.TournamentRepository;
import com.ProjectSoftware.TennisAssignment.entity.Referee;
import com.ProjectSoftware.TennisAssignment.entity.TennisPlayer;
import com.ProjectSoftware.TennisAssignment.entity.Tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/referee")
public class RefereeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MatchRepository matchRepo;

    @Autowired
    private TournamentRepository tournamentRepo;

    @GetMapping("/{username}/matches")
    public List<Match> getRefereeProgram(@PathVariable String username) {
        var referee = (Referee) userRepo.findByUsername(username).orElseThrow();
        return matchRepo.findByReferee(referee);
    }

    @GetMapping("/players")
    public List<TennisPlayer> filterPlayers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer minMatches,
        @RequestParam(required = false) Long tournamentId
    ) {
        List<TennisPlayer> players;

        if (tournamentId != null) {
            Optional<Tournament> tournament = tournamentRepo.findById(tournamentId);
            if (tournament.isPresent()) {
                players = new ArrayList<>(tournament.get().getPlayers());
            } else {
                return List.of(); // invalid ID
            }
        } else {
            players = userRepo.findAll().stream()
                .filter(u -> u instanceof TennisPlayer)
                .map(u -> (TennisPlayer) u)
                .collect(Collectors.toList());
        }

        if (name != null && !name.isBlank()) {
            players = players.stream()
                .filter(p -> p.getFullName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        }

        if (minMatches != null) {
            players = players.stream()
                .filter(p -> matchRepo.countByPlayer1OrPlayer2(p, p) >= minMatches)
                .toList();
        }

        return players;
    }
}
