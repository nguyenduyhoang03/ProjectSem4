package com.TrainingSouls.Repository;


import com.TrainingSouls.Entity.StreakReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StreakRewardRepository extends JpaRepository<StreakReward, Long> {
    boolean existsByUserIdAndStreakDayClaimed(Long userId, int streak);
}


