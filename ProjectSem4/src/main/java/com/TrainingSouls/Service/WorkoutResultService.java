package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.WorkoutResultRequest;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Entity.WorkoutPlan;
import com.TrainingSouls.Entity.WorkoutResult;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.UserProfileRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Repository.WorkoutPlanRepository;
import com.TrainingSouls.Repository.WorkoutResultRepository;
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

@Service
@RequiredArgsConstructor
public class WorkoutResultService {
    private final WorkoutResultRepository workoutResultRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public String saveWorkoutResult(HttpServletRequest httpServletRequest, WorkoutResultRequest request) {
        long userId = JWTUtils.getSubjectFromRequest(httpServletRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.NOT_FOUND.getMessage()));

        WorkoutPlan workoutPlan = workoutPlanRepository.findByUserUserIDAndDayNumber(userId, request.getDayNumber())
                .orElseThrow(() -> new RuntimeException("Workout Plan not found for the given day"));

        // Kiểm tra xem bài tập đã được hoàn thành theo kế hoạch chưa
        String status = determineStatus(workoutPlan, request);

        WorkoutResult workoutResult = new WorkoutResult();
        workoutResult.setUser(user);
        workoutResult.setWorkoutPlan(workoutPlan);
        workoutResult.setSetsCompleted(request.getSetsCompleted());
        workoutResult.setRepsCompleted(request.getRepsCompleted());
        workoutResult.setDistanceCompleted(request.getDistanceCompleted());
        workoutResult.setDurationCompleted(request.getDurationCompleted());
        workoutResult.setStatus(WorkoutPlan.WorkoutStatus.valueOf(status));
        workoutResult.setCreatedAt(LocalDateTime.now());

        workoutResult = workoutResultRepository.save(workoutResult);

        // Cập nhật status của plan
        if ("completed".equals(status)) {
            workoutPlan.setStatus(WorkoutPlan.WorkoutStatus.COMPLETED);
            workoutPlanRepository.save(workoutPlan);
            updateUserStats(userId, workoutPlan.getExercise().getName());
        } else if ("not_completed".equals(status)) {
            workoutPlan.setStatus(WorkoutPlan.WorkoutStatus.NOT_COMPLETED);
            workoutPlanRepository.save(workoutPlan);
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


    @Scheduled(cron = "59 59 23 * * ?") // Chạy mỗi ngày vào 23:59
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
                updateDeathPoints(plan.getUser().getUserID(), yesterday);
            }
        }
    }

    private void updateDeathPoints(Long userId, LocalDate yesterday) {
        boolean missedYesterday = workoutPlanRepository.existsByUserUserIDAndStatusAndWorkoutDate(userId, WorkoutPlan.WorkoutStatus.MISSED, yesterday);

        UserProfile userProfile = userProfileRepository.findByUserUserID(userId).orElse(null);
        if (userProfile == null) return;

        if (missedYesterday) {
            userProfile.setDeathPoints(userProfile.getDeathPoints() + 1);
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

