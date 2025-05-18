package com.ProjectSoftware.TennisAssignment.service;

import com.ProjectSoftware.TennisAssignment.entity.*;
import com.ProjectSoftware.TennisAssignment.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private MatchRepository matchRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private TournamentService tournamentService;

    @Test
    void testGenerateMatches_skipsIfLessThan4Players() {
        Tournament tournament = new Tournament();
        tournament.setPlayers(List.of(
            new TennisPlayer(), new TennisPlayer(), new TennisPlayer()
        ));
        tournament.setStartDate(LocalDate.now());

        tournamentService.generateTournamentMatches(tournament);

        verify(matchRepo, never()).save(any());
    }

    @Test
    void testGenerateMatches_createsMatchesForPoolsAndTree() {
        Tournament tournament = new Tournament();
        tournament.setStartDate(LocalDate.now());

        List<TennisPlayer> players = IntStream.range(0, 8)
            .mapToObj(i -> {
                TennisPlayer p = new TennisPlayer();
                p.setId((long) i + 1);
                p.setUsername("Player" + (i + 1));
                return p;
            }).collect(Collectors.toList());

        tournament.setPlayers(players);

        List<Referee> referees = List.of(new Referee(), new Referee());
        when(userRepo.findAll()).thenReturn(new ArrayList<>(referees));

        tournamentService.generateTournamentMatches(tournament);

        verify(matchRepo, atLeastOnce()).save(any(Match.class));
    }
}

