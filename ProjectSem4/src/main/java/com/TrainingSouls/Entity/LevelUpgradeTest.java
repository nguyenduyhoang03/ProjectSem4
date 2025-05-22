package com.TrainingSouls.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelUpgradeTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String level; // "người mới", "trung cấp"
    String exerciseName; // "Push-up", "Squat", "Sit-up", "Chạy bộ"

    int requiredReps;
    int requiredSets;
    double requiredDuration;
    double requiredDistance;
}

