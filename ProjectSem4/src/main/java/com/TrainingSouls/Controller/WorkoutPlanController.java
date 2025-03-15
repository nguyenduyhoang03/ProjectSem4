package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Response.WorkoutPlanDTO;
import com.TrainingSouls.Entity.WorkoutPlan;
import com.TrainingSouls.Service.WorkoutPlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workout")
@RequiredArgsConstructor
public class WorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;


    @PostMapping("/generate")
    public ResponseEntity<List<WorkoutPlanDTO>> generateWorkout(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(workoutPlanService.generateWorkoutPlan(httpServletRequest));
    }
}

