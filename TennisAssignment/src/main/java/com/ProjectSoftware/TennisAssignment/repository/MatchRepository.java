package com.ProjectSoftware.TennisAssignment.repository;

import com.ProjectSoftware.TennisAssignment.entity.TennisPlayer;
import com.ProjectSoftware.TennisAssignment.entity.Tournament;
import com.ProjectSoftware.TennisAssignment.entity.Match;
import com.ProjectSoftware.TennisAssignment.entity.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByPlayer1OrPlayer2(TennisPlayer p1, TennisPlayer p2);
    List<Match> findByReferee(Referee referee);
    List<Match> findByTournament(Tournament tournament);
    long countByPlayer1OrPlayer2(TennisPlayer p1, TennisPlayer p2);


}
