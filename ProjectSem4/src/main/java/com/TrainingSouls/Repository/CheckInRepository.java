package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.CheckIn;

import com.TrainingSouls.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    boolean existsByUserAndDate(User user, LocalDate date);

    @Query("SELECT c.date FROM CheckIn c WHERE c.user = :user AND c.date BETWEEN :start AND :end")
    List<LocalDate> findCheckInsBetween(User user, LocalDate start, LocalDate end);
}

