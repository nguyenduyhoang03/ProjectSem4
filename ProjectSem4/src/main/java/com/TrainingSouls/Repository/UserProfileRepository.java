package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserUserID(Long userId);

}

