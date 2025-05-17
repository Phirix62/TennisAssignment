package com.ProjectSoftware.TennisAssignment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TournamentRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tournament tournament;

    @ManyToOne
    private TennisPlayer player;

    private String status; // PENDING, ACCEPTED, REJECTED
    private LocalDateTime registeredAt;
}

