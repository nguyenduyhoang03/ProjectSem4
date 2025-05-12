package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.StreakReward;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Repository.PointsTransactionRepository;
import com.TrainingSouls.Repository.StreakRewardRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimStreakReward {
    private final UserRepository userRepository;
    private final StreakRewardRepository streakRewardRepository;
    private final PointsTransactionRepository pointsTransactionRepository;

    public ResponseEntity<String> claimStreakReward(HttpServletRequest request) {
        Long userId = JWTUtils.getSubjectFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow();

        int currentStreak = user.getStreak();

        // Điều kiện: nhận thưởng mỗi 3 ngày liên tiếp
        if (currentStreak < 3 || currentStreak % 3 != 0) {
            return ResponseEntity.badRequest().body("Streak hiện tại không đủ điều kiện nhận thưởng");
        }

        // Đã nhận thưởng cho mốc này chưa?
        if (streakRewardRepository.existsByUserIdAndStreakDayClaimed(userId, currentStreak)) {
            return ResponseEntity.badRequest().body("Bạn đã nhận thưởng cho mốc này rồi");
        }

        // Tính điểm thưởng: có thể nâng cấp sau
        int rewardPoints = 5 + currentStreak / 3 * 2; // ví dụ: mốc 3 = 7 điểm, 6 = 9 điểm, ...

        user.setPoints(user.getPoints() + rewardPoints);
        userRepository.save(user);

        PointsTransaction transaction = PointsTransaction.builder()
                .user(user)
                .type(PointsTransaction.TransactionType.EARN)
                .points(rewardPoints)
                .description("Nhận point streak: " + currentStreak)
                .status(PointsTransaction.TransactionStatus.SUCCESS)
                .date(Instant.now())
                .build();
        pointsTransactionRepository.save(transaction);

        StreakReward reward = StreakReward.builder()
                .userId(userId)
                .streakDayClaimed(currentStreak)
                .claimedAt(LocalDateTime.now())
                .build();
        streakRewardRepository.save(reward);

        return ResponseEntity.ok("Nhận thưởng thành công: +" + rewardPoints + " điểm!");
    }

}
