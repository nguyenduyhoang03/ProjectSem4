package com.TrainingSouls.DTO.Request;

import lombok.Data;

import java.util.List;

@Data
public class DailyWorkoutResultRequest {
    private int dayNumber;
    private List<WorkoutResultRequest> results;

}

