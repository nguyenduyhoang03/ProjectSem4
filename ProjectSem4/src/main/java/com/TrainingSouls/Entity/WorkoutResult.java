package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "workout_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "UserID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @Column(name = "sets_completed", nullable = false)
    private Integer setsCompleted;

    @Column(name = "reps_completed", nullable = false)
    private Integer repsCompleted;

    @Column(name = "distance_completed", nullable = false)
    private Double distanceCompleted = 0.0;

    @Column(name = "duration_completed", nullable = false)
    private Integer durationCompleted;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutPlan.WorkoutStatus status; // "completed", "not_completed", "in_progress"

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

