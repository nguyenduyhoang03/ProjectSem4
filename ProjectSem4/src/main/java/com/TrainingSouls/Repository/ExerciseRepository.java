package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByNameIgnoreCase(String name);
}
