package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Response.WorkoutPlanDTO;
import com.TrainingSouls.Entity.Exercise;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Entity.WorkoutPlan;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.*;
import com.TrainingSouls.Utils.JWTUtils;
import com.TrainingSouls.Utils.WorkoutUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutResultRepository workoutResultRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final CoachStudentRepository coachStudentRepository;
    private final ChatService chatService;

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




    @Transactional
    public List<WorkoutPlanDTO> generateWorkoutPlan(long userId) {
        UserProfile userProfile = userProfileRepository.findByUserUserID(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserProfile not found"));

        if (!workoutPlanRepository.findByUserUserID(userId).isEmpty()) {
            workoutResultRepository.deleteWorkoutPlanByUserUserID(userId);
            workoutPlanRepository.deleteByUserUserID(userId);
        }

        List<Exercise> exercises = exerciseRepository.findAll();
        if (exercises.size() < 4) {
            throw new IllegalStateException("Cần ít nhất 4 bài tập để tạo lịch luyện tập.");
        }

        List<WorkoutPlan> workoutPlans = new ArrayList<>();
        List<WorkoutPlanDTO> result = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        String level = userProfile.getLevel();
        int workoutDayIndex = 0;

        for (int i = 0; i < 30; i++) {
            LocalDate workoutDate = startDate.plusDays(i);

            if (i % 7 == 3 || i % 7 == 6) {
                WorkoutPlan restDayPlan = WorkoutPlan.builder()
                        .user(userProfile.getUser())
                        .exercise(null)
                        .dayNumber(i + 1)
                        .sets(0)
                        .reps(0)
                        .duration(0)
                        .distance(0.0)
                        .workoutDate(workoutDate)
                        .status(WorkoutPlan.WorkoutStatus.NOT_STARTED)
                        .build();

                workoutPlans.add(restDayPlan);

                result.add(new WorkoutPlanDTO(
                        i + 1, "", "", "Nghỉ ngơi", 0, 0, 0,
                        true, 0.0,
                        workoutDate,
                        WorkoutPlan.WorkoutStatus.COMPLETED
                ));
                continue;
            }

            workoutDayIndex++;
            Collections.shuffle(exercises);

            int exercisesPerDay;
            if ("người mới".equalsIgnoreCase(level) && workoutDayIndex <= 7) {
                if (workoutDayIndex == 1) {
                    exercisesPerDay = 1;
                } else if (workoutDayIndex <= 3) {
                    exercisesPerDay = 2;
                } else if (workoutDayIndex <= 5) {
                    exercisesPerDay = 3;
                } else {
                    exercisesPerDay = 4;
                }
            } else {
                exercisesPerDay = 4;
            }

            for (int j = 0; j < exercisesPerDay; j++) {
                Exercise exercise = exercises.get(j);
                int sets = 0, reps = 0, duration = 0;
                double distance = 0.0;

                if ("Chạy bộ".equalsIgnoreCase(exercise.getName())) {
                    distance = WorkoutUtils.getRunningDistance(userProfile);
                    duration = WorkoutUtils.calculateRunningTime(userProfile, distance);
                } else {
                    sets = WorkoutUtils.calculateSets(userProfile);
                    reps = WorkoutUtils.calculateReps(userProfile);
                }

                WorkoutPlan.WorkoutStatus initialStatus =
                        workoutDate.equals(LocalDate.now()) ? WorkoutPlan.WorkoutStatus.NOT_COMPLETED : WorkoutPlan.WorkoutStatus.NOT_STARTED;

                WorkoutPlan workoutPlan = WorkoutPlan.builder()
                        .user(userProfile.getUser())
                        .exercise(exercise)
                        .dayNumber(i + 1)
                        .sets(sets)
                        .reps(reps)
                        .duration(duration)
                        .distance(distance)
                        .workoutDate(workoutDate)
                        .status(initialStatus)
                        .build();

                workoutPlans.add(workoutPlan);

                result.add(new WorkoutPlanDTO(
                        i + 1,
                        exercise.getImg(),
                        exercise.getIcon(),
                        exercise.getName(),
                        sets,
                        reps,
                        duration,
                        false,
                        distance,
                        workoutDate,
                        initialStatus
                ));
            }
        }

        workoutPlanRepository.saveAll(workoutPlans);
        return result;
    }


    @Transactional
    public List<WorkoutPlanDTO> generateWorkoutPlan(HttpServletRequest request){
        long userId = JWTUtils.getSubjectFromRequest(request);
        return generateWorkoutPlan(userId);
    }


    @Transactional
    public void createCustomWorkoutPlanForUser(
            Long targetUserId,
            Long creatorId,
            List<WorkoutPlanDTO> planDTOs) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        boolean isCoach = creator.getRoles().stream()
                .anyMatch(role -> role.getName().equals("COACH"));

        // Kiểm tra quyền
        if (isCoach) {
            boolean allowed = coachStudentRepository.existsByCoach_UserIDAndStudent_UserID(creatorId, targetUserId);
            if (!allowed) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        // Xóa lịch tập cũ nếu có
        workoutPlanRepository.deleteByUserUserID(targetUserId);

        // Tạo lịch mới
        List<WorkoutPlan> plans = new ArrayList<>();

        for (WorkoutPlanDTO dto : planDTOs) {
            Exercise exercise = exerciseRepository.findByNameIgnoreCase(dto.getExerciseName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Không tìm thấy bài tập: " + dto.getExerciseName()));

            WorkoutPlan plan = WorkoutPlan.builder()
                    .user(targetUser)
                    .exercise(exercise)
                    .dayNumber(dto.getDay())
                    .sets(dto.getSets())
                    .reps(dto.getReps())
                    .duration(dto.getDuration())
                    .distance(dto.getDistance())
                    .workoutDate(dto.getWorkoutDate())
                    .status(dto.getStatus() != null ? dto.getStatus() : WorkoutPlan.WorkoutStatus.NOT_STARTED)
                    .build();

            plans.add(plan);
        }

        workoutPlanRepository.saveAll(plans);
    }


    @Transactional
    public void updateCustomWorkoutPlan(Long userId, Long editorId, List<WorkoutPlanDTO> planDTOs) {
        User editor = userRepository.findById(editorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người sửa không tồn tại"));

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người được sửa không tồn tại"));

        boolean isCoach = editor.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("COACH"));

        if (isCoach) {
            boolean allowed = coachStudentRepository.existsByCoach_UserIDAndStudent_UserID(editorId, userId);
            if (!allowed) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không được phép sửa lịch của học viên này.");
            }
        }

        // Lấy các ngày cần cập nhật từ DTO
        List<Integer> daysToUpdate = planDTOs.stream()
                .map(WorkoutPlanDTO::getDay)
                .distinct()
                .collect(Collectors.toList());

        // Xoá tất cả bài tập hiện có của user trong các ngày đó
        workoutPlanRepository.deleteByUserUserIDAndDayNumberIn(userId, daysToUpdate);

        // Tạo mới các kế hoạch
        List<WorkoutPlan> newPlans = new ArrayList<>();

        for (WorkoutPlanDTO dto : planDTOs) {
            Exercise exercise = exerciseRepository.findByNameIgnoreCase(dto.getExerciseName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Không tìm thấy bài tập: " + dto.getExerciseName()));

            WorkoutPlan plan = WorkoutPlan.builder()
                    .user(targetUser)
                    .exercise(exercise)
                    .dayNumber(dto.getDay())
                    .sets(dto.getSets())
                    .reps(dto.getReps())
                    .duration(dto.getDuration())
                    .distance(dto.getDistance())
                    .workoutDate(dto.getWorkoutDate())
                    .status(dto.getStatus() != null ? dto.getStatus() : WorkoutPlan.WorkoutStatus.NOT_STARTED)
                    .build();

            newPlans.add(plan);
        }

        workoutPlanRepository.saveAll(newPlans);
    }



}


