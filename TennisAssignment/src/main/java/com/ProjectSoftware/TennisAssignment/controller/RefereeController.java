package com.ProjectSoftware.TennisAssignment.controller;

import com.ProjectSoftware.TennisAssignment.entity.Match;
import com.ProjectSoftware.TennisAssignment.repository.MatchRepository;
import com.ProjectSoftware.TennisAssignment.repository.UserRepository;
import com.ProjectSoftware.TennisAssignment.entity.Referee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/referee")
public class RefereeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MatchRepository matchRepo;

    @GetMapping("/{username}/matches")
    public List<Match> getRefereeProgram(@PathVariable String username) {
        var referee = (Referee) userRepo.findByUsername(username).orElseThrow();
        return matchRepo.findByReferee(referee);
    }
}
