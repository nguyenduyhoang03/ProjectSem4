package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.DailyWorkoutResultRequest;
import com.TrainingSouls.DTO.Request.WorkoutResultRequest;
import com.TrainingSouls.DTO.Response.WorkoutPlanDTO;
import com.TrainingSouls.Entity.WorkoutPlan;
import com.TrainingSouls.Entity.WorkoutResult;
import com.TrainingSouls.Service.WorkoutPlanService;
import com.TrainingSouls.Service.WorkoutResultService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout")
@RequiredArgsConstructor
public class WorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;
    private final WorkoutResultService workoutResultService;

    @GetMapping
    public ResponseEntity<List<WorkoutPlanDTO>> getWorkoutPlanByUserId(HttpServletRequest request) {
        return ResponseEntity.ok(workoutPlanService.getWorkoutPlanByUserId(request));
    }

    @PostMapping("/generate")
    public ResponseEntity<List<WorkoutPlanDTO>> generateWorkout(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(workoutPlanService.generateWorkoutPlan(httpServletRequest));
    }

    @PostMapping("/workout-results")
    public String submitWorkoutResult(HttpServletRequest httpServletRequest, @RequestBody DailyWorkoutResultRequest request) {
        return workoutResultService.saveDailyWorkoutResults(httpServletRequest,request);
    }

    @GetMapping("/check-missed-workouts")
    public ResponseEntity<String> testCheckMissedWorkouts() {
        workoutResultService.checkMissedWorkouts();
        return ResponseEntity.ok("Cron job executed successfully!");
    }
}

