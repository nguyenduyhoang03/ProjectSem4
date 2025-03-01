package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Integer> {
}
