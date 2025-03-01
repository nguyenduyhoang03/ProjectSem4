package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.PointsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Integer> {
}
