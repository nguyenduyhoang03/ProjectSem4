package com.TrainingSouls.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workout_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "UserID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber; // Ngày tập trong 30 ngày

    @Column(name = "sets", nullable = false)
    private Integer sets;

    @Column(name = "reps", nullable = false)
    private Integer reps;

}

