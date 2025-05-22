package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.CoachStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CoachStudentRepository extends JpaRepository<CoachStudent, Long> {
    boolean existsByCoach_UserIDAndStudent_UserID(Long coachId, Long studentId);

    void deleteByCoach_UserID(Long studentId);

    List<CoachStudent> findByCoach_UserID(long coachId);

    Optional<CoachStudent> findByStudentUserID(long studentId);
}


