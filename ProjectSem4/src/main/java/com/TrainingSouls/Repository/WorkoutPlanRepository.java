package com.TrainingSouls.Repository;

import com.TrainingSouls.DTO.Response.WorkoutPlanDTO;
import com.TrainingSouls.Entity.WorkoutPlan;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Integer> {
    List<WorkoutPlan> findByUserUserID(Long userId);

    @Query("SELECT new com.TrainingSouls.DTO.Response.WorkoutPlanDTO(" +
            "wp.dayNumber, e.img, e.icon, e.name, wp.sets, wp.reps, wp.duration, " +  // Lấy wp.duration thay vì e.duration
            "CASE WHEN e.id IS NULL THEN true ELSE false END, wp.distance, wp.workoutDate, wp.status) " +  // Lấy wp.distance thay vì e.distance
            "FROM WorkoutPlan wp " +
            "LEFT JOIN wp.exercise e " +
            "WHERE wp.user.userID = :userId " +
            "ORDER BY wp.workoutDate, wp.dayNumber")
    List<WorkoutPlanDTO> getWorkoutPlanByUserId(@Param("userId") Long userId);

    Optional<WorkoutPlan> findByUserUserIDAndDayNumberAndExerciseId(Long userId, Integer dayNumber, Long exerciseId);


    // Lấy danh sách các bài tập đã quá hạn nhưng chưa hoàn thành
    List<WorkoutPlan> findByWorkoutDateBeforeAndStatusNot(LocalDate date, WorkoutPlan.WorkoutStatus status);

    // Kiểm tra người dùng có nghỉ tập vào ngày hôm trước không
    boolean existsByUserUserIDAndStatusAndWorkoutDate(Long userId, WorkoutPlan.WorkoutStatus status, LocalDate workoutDate);

    // Đếm số ngày nghỉ liên tiếp gần nhất
    @Query("SELECT COUNT(wp) FROM WorkoutPlan wp " +
            "WHERE wp.user.userID = :userId AND wp.status = 'MISSED' " +
            "AND wp.workoutDate >= :startDate")
    long countMissedDaysInRow(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);


    List<WorkoutPlan> findByWorkoutDateAndStatus(LocalDate today, WorkoutPlan.WorkoutStatus workoutStatus);
}
