package com.TrainingSouls.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutResultRequest {
    private Integer dayNumber;
    private Integer setsCompleted = 0;
    private Integer repsCompleted = 0;
    private Double distanceCompleted = 0.0;
    private Integer durationCompleted = 0;
}

