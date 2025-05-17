package com.ProjectSoftware.TennisAssignment.service;

import com.ProjectSoftware.TennisAssignment.entity.*;
import com.ProjectSoftware.TennisAssignment.repository.MatchRepository;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TournamentService {

    @Autowired MatchRepository matchRepo;
    @Autowired UserRepository userRepo;

    public void generateTournamentMatches(Tournament tournament) {
        List<Match> oldMatches = matchRepo.findByTournament(tournament);
        matchRepo.deleteAll(oldMatches);

        List<TennisPlayer> players = new ArrayList<>(tournament.getPlayers());
        if (players.size() < 4) return;

        Collections.shuffle(players);
        int poolSize = 4;
        int numPools = (int) Math.ceil((double) players.size() / poolSize);
        List<List<TennisPlayer>> pools = new ArrayList<>();

        for (int i = 0; i < numPools; i++) {
            int from = i * poolSize;
            int to = Math.min(from + poolSize, players.size());
            pools.add(new ArrayList<>(players.subList(from, to)));
        }

        List<Referee> referees = userRepo.findAll().stream()
                .filter(u -> u instanceof Referee)
                .map(u -> (Referee) u)
                .toList();

        if (referees.isEmpty()) return;

        LocalDateTime start = tournament.getStartDate().atTime(8, 0);
        LocalDateTime end = start.withHour(19);

        // Pool phase
        for (List<TennisPlayer> pool : pools) {
            for (int i = 0; i < pool.size(); i++) {
                for (int j = i + 1; j < pool.size(); j++) {
                    if (start.getHour() == 12) start = start.plusHours(2);
                    if (start.isAfter(end)) {
                        start = start.withHour(8).plusDays(1);
                        end = start.withHour(19);
                    }

                    Match match = new Match();
                    match.setTournament(tournament);
                    match.setPlayer1(pool.get(i));
                    match.setPlayer2(pool.get(j));
                    match.setMatchTime(start);
                    match.setReferee(referees.get((i + j) % referees.size()));
                    matchRepo.save(match);
                    start = start.plusHours(1);
                }
            }
        }

        // Knockout
        List<TennisPlayer> qualified = pools.stream()
                .flatMap(pool -> pool.stream().limit(2)) // top 2 per pool
                .toList();

        start = start.withHour(8).plusDays(1);
        end = start.withHour(19);

        while (qualified.size() > 1) {
            List<TennisPlayer> next = new ArrayList<>();
            for (int i = 0; i < qualified.size(); i += 2) {
                if (i + 1 < qualified.size()) {
                    if (start.getHour() == 12) start = start.plusHours(2);
                    if (start.isAfter(end)) {
                        start = start.withHour(8).plusDays(1);
                        end = start.withHour(19);
                    }

                    Match knockoutMatch = new Match();
                    knockoutMatch.setTournament(tournament);
                    knockoutMatch.setPlayer1(qualified.get(i));
                    knockoutMatch.setPlayer2(qualified.get(i + 1));
                    knockoutMatch.setMatchTime(start);
                    knockoutMatch.setReferee(referees.get(i % referees.size()));
                    matchRepo.save(knockoutMatch);
                    next.add(qualified.get(i)); // placeholder winner
                    start = start.plusHours(1);
                }
            }
            qualified = next;
        }
    }
}

