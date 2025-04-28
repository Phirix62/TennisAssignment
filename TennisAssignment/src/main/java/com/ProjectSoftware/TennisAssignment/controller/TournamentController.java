package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.Tournament;
import com.ProjectSoftware.TennisAssignment.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepo;

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentRepo.findAll();
    }
}