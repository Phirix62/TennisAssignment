package com.ProjectSoftware.TennisAssignment.repository;

import com.ProjectSoftware.TennisAssignment.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistration, Long> {
    List<TournamentRegistration> findByTournament(Tournament tournament);
    List<TournamentRegistration> findByPlayer(TennisPlayer player);
    Optional<TournamentRegistration> findByTournamentAndPlayer(Tournament tournament, TennisPlayer player);
}