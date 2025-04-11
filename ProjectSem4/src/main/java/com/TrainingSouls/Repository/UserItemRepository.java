package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByExpirationDateBefore(LocalDate now);
}