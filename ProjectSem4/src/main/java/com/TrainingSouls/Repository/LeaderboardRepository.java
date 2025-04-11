package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.Leaderboard;
import com.TrainingSouls.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Integer> {
    Optional<Leaderboard> findByUser(User user);
    List<Leaderboard> findAllByOrderByTotalScoreDesc();
}
