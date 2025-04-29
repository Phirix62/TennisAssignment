package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.Match;
import com.ProjectSoftware.TennisAssignment.entity.Referee;
import com.ProjectSoftware.TennisAssignment.entity.TennisPlayer;
import com.ProjectSoftware.TennisAssignment.entity.Tournament;
import com.ProjectSoftware.TennisAssignment.repository.MatchRepository;
import com.ProjectSoftware.TennisAssignment.repository.TournamentRepository;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    // Player joins a tournament
    @PostMapping("/{username}/join/{tournamentId}")
    public String joinTournament(@PathVariable String username, @PathVariable Long tournamentId) {
        TennisPlayer player = (TennisPlayer) userRepo.findByUsername(username).orElseThrow();
        Tournament tournament = tournamentRepo.findById(tournamentId).orElseThrow();

        if (tournament.getPlayers().contains(player)) {
            return "Player is already registered for this tournament.";
        }

        // Add player to tournament
        tournament.getPlayers().add(player);
        tournamentRepo.save(tournament);

        // Delete old matches for this tournament
        List<Match> oldMatches = matchRepo.findByTournament(tournament);
        matchRepo.deleteAll(oldMatches);

        // Regenerate matches
        List<TennisPlayer> players = tournament.getPlayers();
        if (players.size() >= 2) {
            Collections.shuffle(players); // Random shuffle

            // Get list of referees
            List<Referee> referees = userRepo.findAll().stream()
                    .filter(u -> u instanceof Referee)
                    .map(u -> (Referee) u)
                    .toList();

            if (referees.isEmpty()) {
                return "No referees available to assign matches.";
            }

            for (int i = 0; i < players.size() - 1; i += 2) {
                Match match = new Match();
                match.setTournament(tournament);
                match.setPlayer1(players.get(i));
                match.setPlayer2(players.get(i + 1));
                match.setMatchTime(LocalDateTime.now().plusDays(i)); // for example
                match.setReferee(referees.get(i % referees.size())); // assign referees cyclically
                matchRepo.save(match);
            }

            // If odd number of players: last player waits (no match)
            if (players.size() % 2 != 0) {
                // Optionally: inform last player is waiting
            }
        }

        return "Player " + username + " successfully joined tournament and matches generated.";
    }

    // Player views his matches
    @GetMapping("/{username}/matches")
    public List<Match> getPlayerMatches(@PathVariable String username) {
        TennisPlayer player = (TennisPlayer) userRepo.findByUsername(username).orElseThrow();
        return matchRepo.findByPlayer1OrPlayer2(player, player);
    }
}

