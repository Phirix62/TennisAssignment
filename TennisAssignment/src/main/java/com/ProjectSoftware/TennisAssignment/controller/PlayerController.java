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

        // Regenerate matches based on tournament format
        List<TennisPlayer> players = tournament.getPlayers();
        if (players.size() >= 4) { // Minimum players for a pool phase
            Collections.shuffle(players); // Random shuffle

            // Pool phase
            int poolSize = 4; // Example: 4 players per pool
            int numberOfPools = (int) Math.ceil((double) players.size() / poolSize);
            List<List<TennisPlayer>> pools = new java.util.ArrayList<>();

            for (int i = 0; i < numberOfPools; i++) {
                pools.add(new ArrayList<>(players.subList(i * poolSize, Math.min((i + 1) * poolSize, players.size()))));
            }

            // Get list of referees
            List<Referee> referees = userRepo.findAll().stream()
                    .filter(u -> u instanceof Referee)
                    .map(u -> (Referee) u)
                    .toList();

            if (referees.isEmpty()) {
                return "No referees available to assign matches.";
            }

            // Generate pool matches
            LocalDateTime startTime = tournament.getStartDate().atTime(8, 0);
            LocalDateTime endTime = startTime.plusHours(11); // End time is 11 hours after start time
            for (List<TennisPlayer> pool : pools) {
                for (int i = 0; i < pool.size(); i++) {
                    for (int j = i + 1; j < pool.size(); j++) {
                        if (startTime.getHour() == 12) {
                            startTime = startTime.plusHours(2); // Skip break time
                        }
                        if (startTime.isAfter(endTime)) {
                            startTime = startTime.withHour(8).plusDays(1); // Move to the next day
                            endTime = startTime.withHour(19);
                        }

                        Match match = new Match();
                        match.setTournament(tournament);
                        match.setPlayer1(pool.get(i));
                        match.setPlayer2(pool.get(j));
                        match.setMatchTime(startTime);
                        match.setReferee(referees.get((i + j) % referees.size())); // Assign referees cyclically
                        matchRepo.save(match);

                        startTime = startTime.plusHours(1); // Increment match time
                    }
                }
            }

            // Tree phase (knockout)
            List<TennisPlayer> qualifiedPlayers = pools.stream()
                    .flatMap(pool -> pool.stream().limit(2)) // Simulate top 2 from each pool
                    .toList();


            startTime = startTime.withHour(8).plusDays(1); // Start tree phase on day 2
            endTime = startTime.withHour(19);

            while (qualifiedPlayers.size() > 1) {
                List<TennisPlayer> nextRound = new java.util.ArrayList<>();
                for (int i = 0; i < qualifiedPlayers.size(); i += 2) {
                    if (i + 1 < qualifiedPlayers.size()) {
                        if (startTime.getHour() == 12) {
                            startTime = startTime.plusHours(2); // Skip break time
                        }
                        if (startTime.isAfter(endTime)) {
                            startTime = startTime.withHour(8).plusDays(1); // Move to next day
                            endTime = startTime.withHour(19);
                        }

                        Match match = new Match();
                        match.setTournament(tournament);
                        match.setPlayer1(qualifiedPlayers.get(i));
                        match.setPlayer2(qualifiedPlayers.get(i + 1));
                        match.setMatchTime(startTime);
                        match.setReferee(referees.get(i % referees.size())); // Assign referees cyclically
                        matchRepo.save(match);

                        // Example: winner is the first player (replace with actual logic)
                        nextRound.add(qualifiedPlayers.get(i));

                        startTime = startTime.plusHours(1); // Increment match time
                    }
                }
                qualifiedPlayers = nextRound;
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

