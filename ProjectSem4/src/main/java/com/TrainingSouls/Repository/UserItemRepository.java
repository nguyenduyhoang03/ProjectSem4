package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByExpirationDateBefore(LocalDate now);
    Optional<UserItem> findByUserUserIDAndItemId(Long userId, Integer itemId);

}