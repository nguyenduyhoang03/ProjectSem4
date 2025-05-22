package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.MealSuggestion;
import com.TrainingSouls.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MealSuggestionRepository extends JpaRepository<MealSuggestion, Integer> {
    Optional<MealSuggestion> findByUserAndDate(User user, LocalDate date);
}
