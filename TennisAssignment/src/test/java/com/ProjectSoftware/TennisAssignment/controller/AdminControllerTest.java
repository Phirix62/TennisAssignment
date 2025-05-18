package com.ProjectSoftware.TennisAssignment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import com.ProjectSoftware.TennisAssignment.repository.*;
import com.ProjectSoftware.TennisAssignment.service.*;
import com.ProjectSoftware.TennisAssignment.entity.*;
import java.util.*;






@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentRegistrationRepository registrationRepo;

    @MockBean
    private TournamentRepository tournamentRepo;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private MatchRepository matchRepo;

    @MockBean
    private MailService mailService;

    @MockBean
    private TournamentService tournamentService;

    @Test
    void testUpdateRegistrationStatus_successfulAccept() throws Exception {
        TennisPlayer player = new TennisPlayer();
        player.setId(1L);
        player.setFullName("John");
        player.setEmail("john@email.com");

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("Wimbledon");
        tournament.setPlayers(new ArrayList<>());

        TournamentRegistration reg = new TournamentRegistration();
        reg.setId(99L);
        reg.setPlayer(player);
        reg.setTournament(tournament);
        reg.setStatus("PENDING");

        when(registrationRepo.findById(99L)).thenReturn(Optional.of(reg));
        when(registrationRepo.findByTournament(tournament)).thenReturn(List.of(reg));

        mockMvc.perform(put("/admin/registration/99/status")
                .param("status", "ACCEPTED"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ACCEPTED")));

        verify(registrationRepo).save(any());
        verify(tournamentRepo).save(any());
        verify(mailService).sendRegistrationDecision(
                eq(player.getEmail()),
                eq(player.getFullName()),
                eq(tournament.getName()),
                eq("ACCEPTED"));
    }

    @Test
    void testUpdateRegistrationStatus_notFound() throws Exception {
        when(registrationRepo.findById(123L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/admin/registration/123/status")
                .param("status", "ACCEPTED"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Registration not found")));
    }

    @Test
    void testUpdateRegistrationStatus_invalidStatus() throws Exception {
        TournamentRegistration reg = new TournamentRegistration();
        reg.setId(88L);
        reg.setPlayer(new TennisPlayer());
        reg.setTournament(new Tournament());

        when(registrationRepo.findById(88L)).thenReturn(Optional.of(reg));

        mockMvc.perform(put("/admin/registration/88/status")
                .param("status", "MAYBE"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid status")));
    }
}
