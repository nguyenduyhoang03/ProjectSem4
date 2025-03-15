package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Response.WorkoutPlanDTO;
import com.TrainingSouls.Entity.Exercise;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Entity.WorkoutPlan;
import com.TrainingSouls.Repository.ExerciseRepository;
import com.TrainingSouls.Repository.UserProfileRepository;
import com.TrainingSouls.Repository.WorkoutPlanRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserProfileRepository userProfileRepository;




    public List<WorkoutPlanDTO> generateWorkoutPlan(HttpServletRequest httpServletRequest) {
        long userId = JWTUtils.getSubjectFromRequest(httpServletRequest);
        UserProfile userProfile = userProfileRepository.findByUserUserID(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserProfile not found"));

        List<Exercise> exercises = exerciseRepository.findAll();
        List<WorkoutPlan> workoutPlans = new ArrayList<>();
        List<WorkoutPlanDTO> result = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 30; i++) {
            if (i % 7 == 4 || i % 7 == 0) { // Nghỉ vào thứ 4 và Chủ nhật
                // 🛑 Lưu ngày nghỉ vào database
                WorkoutPlan restDayPlan = new WorkoutPlan(null, userProfile.getUser(), null, i, 0, 0);
                workoutPlans.add(restDayPlan);

                result.add(new WorkoutPlanDTO(i, null, "Nghỉ ngơi", null, 0, 0, 0, true, null));
                continue;
            }

            Exercise exercise = exercises.get(random.nextInt(exercises.size()));
            int sets = 0, reps = 0, duration = 0;
            Double distance = 0.0;

            if (exercise.getName().equalsIgnoreCase("Chạy bộ")) {
                distance = getRunningDistance(userProfile);
                duration = calculateRunningTime(userProfile, distance);
            } else {
                sets = calculateSets(userProfile);
                reps = calculateReps(userProfile);
            }

            WorkoutPlan workoutPlan = new WorkoutPlan(null, userProfile.getUser(), exercise, i, sets, reps);
            workoutPlans.add(workoutPlan);

            result.add(new WorkoutPlanDTO(i, exercise.getImg(), exercise.getIcon(), exercise.getName(), sets, reps, duration, false, distance));
        }

        workoutPlanRepository.saveAll(workoutPlans);
        return result;
    }



    private int calculateSets(UserProfile userProfile) {
        return switch (userProfile.getLevel().toLowerCase()) {
            case "beginner" -> 3;
            case "intermediate" -> 4;
            case "advanced" -> 5;
            default -> 3;
        };
    }

    private int calculateReps(UserProfile userProfile) {
        if (userProfile.getBmi() < 18.5) return 12;
        if (userProfile.getBmi() < 25) return 15;
        return 10;
    }

    private double getRunningDistance(UserProfile userProfile) {
        return switch (userProfile.getLevel().toLowerCase()) {
            case "beginner" -> 2.0;      // Người mới chạy 2km
            case "intermediate" -> 3.0;  // Trung cấp chạy 3km
            case "advanced" -> 5.0;      // Nâng cao chạy 5km
            default -> 2.0;
        };
    }

    private int calculateRunningTime(UserProfile userProfile, double distance) {
        double pace = switch (userProfile.getLevel().toLowerCase()) {
            case "beginner" -> 8.0;  // 8 phút/km
            case "intermediate" -> 6.5;
            case "advanced" -> 5.5;
            default -> 8.0;
        };
        return (int) Math.round(distance * pace); // Tổng thời gian chạy
    }

}


