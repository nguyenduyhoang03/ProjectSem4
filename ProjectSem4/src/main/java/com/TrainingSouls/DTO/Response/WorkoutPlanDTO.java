package com.TrainingSouls.DTO.Response;

import com.TrainingSouls.Entity.WorkoutPlan;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlanDTO {
    private Integer day;
    private String img;
    private String icon;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Integer duration;
    private boolean restDay;
    private Double distance;
    private LocalDate workoutDate;
    private WorkoutPlan.WorkoutStatus status;
}
