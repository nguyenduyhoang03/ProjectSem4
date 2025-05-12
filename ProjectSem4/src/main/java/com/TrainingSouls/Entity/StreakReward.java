package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "streak_rewards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreakReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int streakDayClaimed;

    private LocalDateTime claimedAt;

}

