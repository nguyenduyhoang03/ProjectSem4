package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.DailyWorkoutResultRequest;
import com.TrainingSouls.DTO.Request.WorkoutResultRequest;
import com.TrainingSouls.DTO.Response.WorkoutResultDTO;
import com.TrainingSouls.Entity.*;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Mapper.WorkoutResultMapper;
import com.TrainingSouls.Repository.*;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutResultService {
    private final WorkoutResultRepository workoutResultRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutResultMapper mapper;

    @Transactional
    public String saveDailyWorkoutResults(HttpServletRequest httpServletRequest, DailyWorkoutResultRequest request) {
        long userId = JWTUtils.getSubjectFromRequest(httpServletRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.NOT_FOUND.getMessage()));

        boolean allCompleted = true;

        for (WorkoutResultRequest resultRequest : request.getResults()) {

            // Lấy bài tập theo tên
            Exercise exercise = exerciseRepository.findByNameIgnoreCase(resultRequest.getExerciseName())
                    .orElseThrow(() -> new RuntimeException("Exercise not found: " + resultRequest.getExerciseName()));

            // Lấy kế hoạch tập luyện theo ngày, người dùng và bài tập
            WorkoutPlan workoutPlan = workoutPlanRepository
                    .findByUserUserIDAndDayNumberAndExerciseId(userId, request.getDayNumber(), exercise.getId())
                    .orElseThrow(() -> new RuntimeException("Workout Plan not found for exercise: " + exercise.getName()));

            String status = determineStatus(workoutPlan, resultRequest);

            WorkoutResult workoutResult = new WorkoutResult();
            workoutResult.setUser(user);
            workoutResult.setWorkoutPlan(workoutPlan);
            workoutResult.setSetsCompleted(resultRequest.getSetsCompleted());
            workoutResult.setRepsCompleted(resultRequest.getRepsCompleted());
            workoutResult.setDistanceCompleted(resultRequest.getDistanceCompleted());
            workoutResult.setDurationCompleted(resultRequest.getDurationCompleted());
            workoutResult.setStatus(WorkoutPlan.WorkoutStatus.valueOf(status.toUpperCase()));
            workoutResult.setCreatedAt(LocalDateTime.now());

            workoutResultRepository.save(workoutResult);

            // Cập nhật trạng thái kế hoạch
            WorkoutPlan.WorkoutStatus planStatus = WorkoutPlan.WorkoutStatus.valueOf(status.toUpperCase());
            workoutPlan.setStatus(planStatus);
            workoutPlanRepository.save(workoutPlan);

            if (planStatus != WorkoutPlan.WorkoutStatus.COMPLETED) {
                allCompleted = false;
            } else {
                updateUserStats(userId, exercise.getName());
            }
        }

        // Logic cộng điểm tổng nếu tất cả hoàn thành
        if (allCompleted) {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserUserID(userId);
            userProfileOpt.ifPresent(profile -> {
                profile.setStrength(profile.getStrength() + 1);
                profile.setEndurance(profile.getEndurance() + 1);
                profile.setHealth(profile.getHealth() + 1);
                profile.setAgility(profile.getAgility() + 1);
                userProfileRepository.save(profile);
            });

            user.setStreak(user.getStreak() + 1);
        }

        return "Success";
    }




    private void updateUserStats(Long userId, String exerciseName) {
        try {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserUserID(userId);

            if (userProfileOpt.isEmpty()) {
                System.out.println("Không tìm thấy UserProfile cho userId = " + userId);
                return;
            }

            UserProfile userProfile = userProfileOpt.get();
            System.out.println("Bài tập thực hiện: " + exerciseName);

            switch (exerciseName.trim().toLowerCase()) {
                case "hít đất":
                    userProfile.setStrength(userProfile.getStrength() + 5);
                    break;
                case "gập bụng":
                    userProfile.setEndurance(userProfile.getEndurance() + 5);
                    break;
                case "squat":
                    userProfile.setHealth(userProfile.getHealth() + 5);
                    break;
                case "chạy bộ":
                    userProfile.setAgility(userProfile.getAgility() + 5);
                    break;
                default:
                    System.out.println("Không tìm thấy bài tập phù hợp!");
                    return;
            }

            userProfileRepository.save(userProfile);
            System.out.println("Cập nhật UserProfile thành công!");

        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật UserProfile: " + e.getMessage());
        }
    }


    private String determineStatus(WorkoutPlan workoutPlan, WorkoutResultRequest request) {
        if (request.getSetsCompleted() >= workoutPlan.getSets() &&
                request.getRepsCompleted() >= workoutPlan.getReps()) {
            return "completed";
        }
        return "not_completed";
    }

    public List<WorkoutResultDTO> getWorkoutHistory(HttpServletRequest request, LocalDate start, LocalDate end) {
        long userId = JWTUtils.getSubjectFromRequest(request);

        List<WorkoutResult> results;

        if (start != null && end != null) {
            results = workoutResultRepository.findByUserUserIDAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start.atStartOfDay(), end.atTime(23, 59, 59));
        } else {
            results = workoutResultRepository.findByUserUserIDOrderByCreatedAtDesc(userId);
        }

        return results.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }



    @Scheduled(cron = "0 3 0 * * ?") // Chạy mỗi ngày lúc 00:01
    @Transactional
    public void updateWorkoutStatusToNotCompleted() {
        LocalDate today = LocalDate.now();

        // Tìm các bài tập hôm nay đang có trạng thái NOT_STARTED
        List<WorkoutPlan> todayPlans = workoutPlanRepository
                .findByWorkoutDateAndStatus(today, WorkoutPlan.WorkoutStatus.NOT_STARTED);

        for (WorkoutPlan plan : todayPlans) {
            plan.setStatus(WorkoutPlan.WorkoutStatus.NOT_COMPLETED);
        }

    }



    @Scheduled(cron = "0 0 0 * * ?") // Chạy mỗi ngày vào 00:00
    @Transactional
    public void checkMissedWorkouts() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // Lấy tất cả lịch tập đã quá hạn nhưng chưa hoàn thành
        List<WorkoutPlan> missedWorkouts = workoutPlanRepository.findByWorkoutDateBeforeAndStatusNot(today, WorkoutPlan.WorkoutStatus.COMPLETED);

        for (WorkoutPlan plan : missedWorkouts) {
            // Kiểm tra xem ngày này đã có kết quả tập chưa
            boolean hasResult = workoutResultRepository.existsByWorkoutPlan(plan);
            if (!hasResult) {
                plan.setStatus(WorkoutPlan.WorkoutStatus.MISSED);
                workoutPlanRepository.save(plan);

                // Cộng deathPoint nếu người dùng nghỉ 2 ngày liên tiếp
                updateDeathPointsAndStreaks(plan.getUser().getUserID(), yesterday);

            }
        }
    }

    private void updateDeathPointsAndStreaks(Long userId, LocalDate yesterday) {
        boolean missedYesterday = workoutPlanRepository.existsByUserUserIDAndStatusAndWorkoutDate(userId, WorkoutPlan.WorkoutStatus.MISSED, yesterday);


        UserProfile userProfile = userProfileRepository.findByUserUserID(userId).orElse(null);
        if (userProfile == null) return;

        if (missedYesterday) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(ErrorCode.NOT_FOUND.getMessage()));
            userProfile.setDeathPoints(userProfile.getDeathPoints() + 1);
            user.setStreak(0);
        }

        // Nếu nghỉ 15 ngày liên tiếp => reset chỉ số về 1
        long missedDays = workoutPlanRepository.countMissedDaysInRow(userId, LocalDate.now().minusDays(14));
        if (missedDays >= 15) {
            resetUserStats(userProfile);
        }

        userProfileRepository.save(userProfile);
    }

    private void resetUserStats(UserProfile userProfile) {
        userProfile.setStrength(1);
        userProfile.setEndurance(1);
        userProfile.setHealth(1);
        userProfile.setAgility(1);
    }
}

