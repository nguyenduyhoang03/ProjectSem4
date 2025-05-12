package com.TrainingSouls.DTO.Response;

import com.TrainingSouls.Entity.WorkoutPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResultDTO {
    private String exerciseName;
    private Integer setsCompleted;
    private Integer repsCompleted;
    private Double distanceCompleted;
    private Integer durationCompleted;
    private String status;
    private LocalDateTime createdAt;
}
