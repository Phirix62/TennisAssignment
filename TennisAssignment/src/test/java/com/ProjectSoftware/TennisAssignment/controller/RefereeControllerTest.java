package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.repository.*;
import com.ProjectSoftware.TennisAssignment.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@WebMvcTest(RefereeController.class)
class RefereeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private MatchRepository matchRepo;

    @MockBean
    private TournamentRepository tournamentRepo;

    @Test
    void testFilterByName() throws Exception {
        TennisPlayer p1 = new TennisPlayer();
        p1.setId(1L);
        p1.setFullName("Nathan");
        TennisPlayer p2 = new TennisPlayer();
        p2.setId(2L);
        p2.setFullName("Emma");

        when(userRepo.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/referee/players").param("name", "nat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Nathan"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFilterByMinMatches() throws Exception {
        TennisPlayer p1 = new TennisPlayer();
        p1.setId(1L);
        p1.setFullName("Player1");
        TennisPlayer p2 = new TennisPlayer();
        p2.setId(2L);
        p2.setFullName("Player2");

        when(userRepo.findAll()).thenReturn(List.of(p1, p2));
        when(matchRepo.countByPlayer1OrPlayer2(p1, p1)).thenReturn(3L);
        when(matchRepo.countByPlayer1OrPlayer2(p2, p2)).thenReturn(1L);

        mockMvc.perform(get("/referee/players").param("minMatches", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Player1"));
    }

    @Test
    void testFilterByTournament() throws Exception {
        TennisPlayer p1 = new TennisPlayer();
        p1.setId(1L);
        p1.setFullName("PlayerA");

        Tournament t = new Tournament();
        t.setId(10L);
        t.setName("Test Tournament");
        t.setPlayers(List.of(p1));

        when(tournamentRepo.findById(10L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/referee/players").param("tournamentId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("PlayerA"));
    }

    @Test
    void testFilterByInvalidTournament() throws Exception {
        when(tournamentRepo.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/referee/players").param("tournamentId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
