package com.TrainingSouls.DTO.Response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanDTO {
    private int day;
    private String img;
    private String icon;
    private String exerciseName;
    private int sets;
    private int reps;
    private int duration;
    private boolean restDay;
    private Double distance;

}


