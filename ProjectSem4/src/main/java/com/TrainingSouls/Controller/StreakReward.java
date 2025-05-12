package com.TrainingSouls.Controller;

import com.TrainingSouls.Service.ClaimStreakReward;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reward")
@RequiredArgsConstructor
public class StreakReward {
    private final ClaimStreakReward claimStreakRewardService;

    @PostMapping
    public ResponseEntity<String> claimRewardStreak(HttpServletRequest request) {
        return  claimStreakRewardService.claimStreakReward(request);
    }
}

