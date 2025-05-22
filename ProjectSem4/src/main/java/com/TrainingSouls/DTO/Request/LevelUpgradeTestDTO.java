package com.TrainingSouls.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelUpgradeTestDTO {
    private String exerciseName;
    private int reps;
    private int sets;
    private double duration;
    private double distance;
}
