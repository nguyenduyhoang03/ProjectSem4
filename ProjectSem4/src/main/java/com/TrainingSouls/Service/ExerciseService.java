package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.Exercise;
import com.TrainingSouls.Repository.ExerciseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseService {

    ExerciseRepository exerciseRepository;

    // Create
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    // Read All
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    // Read by ID
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Exercise not found with id: " + id));
    }

    // Update
    public Exercise updateExercise(Long id, Exercise updatedExercise) {
        Exercise existing = getExerciseById(id);
        existing.setName(updatedExercise.getName());
        existing.setImg(updatedExercise.getImg());
        existing.setIcon(updatedExercise.getIcon());
        existing.setDescription(updatedExercise.getDescription());
        return exerciseRepository.save(existing);
    }

    // Delete
    public void deleteExercise(Long id) {
        Exercise existing = getExerciseById(id);
        exerciseRepository.delete(existing);
    }

    // Optional: Find by name (ignore case)
    public Exercise findByNameIgnoreCase(String name) {
        return exerciseRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NoSuchElementException("Exercise not found with name: " + name));
    }
}

