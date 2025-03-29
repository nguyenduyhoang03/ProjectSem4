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

import java.time.LocalDate;
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

    public List<WorkoutPlanDTO> getWorkoutPlanByUserId(HttpServletRequest request) {
        long userId = JWTUtils.getSubjectFromRequest(request);
        return workoutPlanRepository.getWorkoutPlanByUserId(userId)
                .stream()
                .map(dto -> new WorkoutPlanDTO(
                        dto.getDay(),
                        dto.getImg() != null ? dto.getImg() : "",
                        dto.getIcon() != null ? dto.getIcon() : "",
                        dto.getExerciseName() != null ? dto.getExerciseName() : "Nghỉ ngơi",
                        dto.getSets(),
                        dto.getReps(),
                        dto.getDuration() != null ? dto.getDuration() : 0,
                        dto.isRestDay(),
                        dto.getDistance() != null ? dto.getDistance() : 0.0,
                        dto.getWorkoutDate(),
                        dto.getStatus()
                ))
                .collect(Collectors.toList());
    }




    public List<WorkoutPlanDTO> generateWorkoutPlan(HttpServletRequest httpServletRequest) {
        long userId = JWTUtils.getSubjectFromRequest(httpServletRequest);
        UserProfile userProfile = userProfileRepository.findByUserUserID(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserProfile not found"));

        List<Exercise> exercises = exerciseRepository.findAll();
        List<WorkoutPlan> workoutPlans = new ArrayList<>();
        List<WorkoutPlanDTO> result = new ArrayList<>();
        Random random = new Random();

        LocalDate startDate = LocalDate.now(); // Ngày bắt đầu lịch tập

        for (int i = 0; i < 30; i++) {
            LocalDate workoutDate = startDate.plusDays(i); // Tính ngày tập dựa vào ngày bắt đầu

            if (i % 7 == 3 || i % 7 == 6) { // Nghỉ vào thứ 4 và Chủ nhật (i % 7 bắt đầu từ 0)
                WorkoutPlan restDayPlan = new WorkoutPlan(null, userProfile.getUser(), null, i + 1, 0, 0, 0, 0.0, workoutDate, WorkoutPlan.WorkoutStatus.NOT_STARTED);
                workoutPlans.add(restDayPlan);

                result.add(new WorkoutPlanDTO(i + 1, "", "", "Nghỉ ngơi", 0, 0, 0, true, 0.0,workoutDate ,WorkoutPlan.WorkoutStatus.COMPLETED));
                continue;
            }

            Exercise exercise = exercises.get(random.nextInt(exercises.size()));
            int sets = 0, reps = 0, duration = 0;
            double distance = 0.0;

            if (exercise.getName().equalsIgnoreCase("Chạy bộ")) {
                distance = getRunningDistance(userProfile);
                duration = calculateRunningTime(userProfile, distance);
            } else {
                sets = calculateSets(userProfile);
                reps = calculateReps(userProfile);
            }

            WorkoutPlan workoutPlan = new WorkoutPlan(null, userProfile.getUser(), exercise, i + 1, sets, reps, duration, distance, workoutDate, WorkoutPlan.WorkoutStatus.NOT_STARTED);
            workoutPlans.add(workoutPlan);

            result.add(new WorkoutPlanDTO(i + 1, exercise.getImg(), exercise.getIcon(), exercise.getName(), sets, reps, duration, false, distance, workoutDate, WorkoutPlan.WorkoutStatus.NOT_STARTED));
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
        double bmi = userProfile.getBmi();

        if (bmi < 18.5) return 12;      // Gầy
        if (bmi >= 18.5 && bmi < 25) return 15; // Bình thường
        if (bmi >= 25 && bmi < 30) return 12; // Thừa cân
        if (bmi >= 30 && bmi < 35) return 10; // Béo phì độ 1
        if (bmi >= 35 && bmi < 40) return 8;  // Béo phì độ 2
        return 6;                          // Béo phì độ 3
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


