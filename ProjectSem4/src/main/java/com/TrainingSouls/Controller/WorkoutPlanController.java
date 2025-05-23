package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.DailyWorkoutResultRequest;
import com.TrainingSouls.DTO.Response.WorkoutPlanDTO;
import com.TrainingSouls.DTO.Response.WorkoutResultDTO;
import com.TrainingSouls.Service.WorkoutPlanService;
import com.TrainingSouls.Service.WorkoutResultService;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/history")
    public ResponseEntity<List<WorkoutResultDTO>> getWorkoutHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            HttpServletRequest request) {

        List<WorkoutResultDTO> history = workoutResultService.getWorkoutHistory(request, start, end);

        return ResponseEntity.ok(history);
    }



    @GetMapping("/check-missed-workouts")
    public ResponseEntity<String> testCheckMissedWorkouts() {
        workoutResultService.checkMissedWorkouts();
        return ResponseEntity.ok("Cron job executed successfully!");
    }


    @PreAuthorize("hasRole('COACH')")
    @PostMapping("/custom/{userId}")
    public ResponseEntity<?> createCustomPlan(
            @PathVariable Long userId,
            @RequestBody List<WorkoutPlanDTO> plans,
            HttpServletRequest request) {

        Long creatorId = JWTUtils.getSubjectFromRequest(request);
        workoutPlanService.createCustomWorkoutPlanForUser(userId, creatorId, plans);
        return ResponseEntity.ok("Lên lịch thủ công thành công");
    }

    @PreAuthorize("hasRole('COACH')")
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateCustomWorkoutPlan(
            @PathVariable Long userId,
            @RequestBody List<WorkoutPlanDTO> plans,
            HttpServletRequest request) {

        Long editorId = JWTUtils.getSubjectFromRequest(request);
        workoutPlanService.updateCustomWorkoutPlan(userId, editorId, plans);
        return ResponseEntity.ok("Cập nhật lịch tập thành công");
    }


}

