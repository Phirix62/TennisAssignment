package com.ProjectSoftware.TennisAssignment.repository;

import com.ProjectSoftware.TennisAssignment.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
