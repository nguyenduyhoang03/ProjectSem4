package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Integer> {
    List<WorkoutPlan> findByUserUserID(Long userId);
}
