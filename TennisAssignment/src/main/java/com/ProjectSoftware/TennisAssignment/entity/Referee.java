package com.ProjectSoftware.TennisAssignment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("REFEREE")
public class Referee extends User {

}