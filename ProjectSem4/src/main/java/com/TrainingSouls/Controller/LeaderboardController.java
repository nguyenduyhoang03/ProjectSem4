package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Response.LeaderboardDTO;
import com.TrainingSouls.Service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranks")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<LeaderboardDTO>> getAllLeaderboard() {
        return leaderboardService.getAllLeaderboard();
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateLeaderboard() {
        leaderboardService.updateLeaderboard();
        return ResponseEntity.ok("Leaderboard updated successfully");
    }
}
