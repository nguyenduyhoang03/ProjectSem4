package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.LevelUpgradeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelUpgradeTestRepository extends JpaRepository<LevelUpgradeTest, Integer> {
    List<LevelUpgradeTest> findByLevel(String level);
}
