package com.ProjectSoftware.TennisAssignment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PLAYER")
public class TennisPlayer extends User {
   
}

