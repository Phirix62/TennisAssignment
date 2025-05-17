package com.ProjectSoftware.TennisAssignment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
public class TournamentRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"players", "matches", "registrations"})
    private Tournament tournament;

    @ManyToOne
    @JsonIgnoreProperties({"tournaments", "matches"})
    private TennisPlayer player;

    private String status; // PENDING, ACCEPTED, REJECTED
    private LocalDateTime registeredAt;
}

