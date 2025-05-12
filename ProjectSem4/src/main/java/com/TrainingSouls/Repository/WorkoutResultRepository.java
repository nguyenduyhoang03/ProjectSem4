package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.WorkoutPlan;
import com.TrainingSouls.Entity.WorkoutResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutResultRepository extends JpaRepository<WorkoutResult, Long> {
    Optional<WorkoutResult> findByUserUserIDAndWorkoutPlanId(Long userId, Long workoutPlanId);

    boolean existsByWorkoutPlan(WorkoutPlan plan);

    List<WorkoutResult> findByUserUserIDOrderByCreatedAtDesc(Long userId);

    List<WorkoutResult> findByUserUserIDAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long userId, LocalDateTime start, LocalDateTime end);
}

