package com.TrainingSouls.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLevelUpgradeTest {
    private Long id;
    private String level;
    private String exerciseName;
    private int requiredReps;
    private int requiredSets;
    private double requiredDuration;
    private double requiredDistance;
}

