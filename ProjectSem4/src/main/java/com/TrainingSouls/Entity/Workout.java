package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workouts")
public class Workout {
    @Id
    @Column(name = "WorkoutID", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Lob
    @Column(name = "Type", nullable = false)
    private String type;

    @Lob
    @Column(name = "Difficulty", nullable = false)
    private String difficulty;

}