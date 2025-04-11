package com.TrainingSouls.DTO.Response;

import com.TrainingSouls.Entity.Leaderboard;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardDTO {
    private Long id;
    private int strengthScore;
    private int enduranceScore;
    private int healthScore;
    private int agilityScore;
    private int deathpoints;
    private double totalScore;
    private int rank;
    private String userName;

    public LeaderboardDTO(Leaderboard leaderboard) {
        this.id = leaderboard.getId();
        this.strengthScore = leaderboard.getStrengthScore();
        this.enduranceScore = leaderboard.getEnduranceScore();
        this.healthScore = leaderboard.getHealthScore();
        this.agilityScore = leaderboard.getAgilityScore();
        this.deathpoints = leaderboard.getDeathpoints();
        this.totalScore = leaderboard.getTotalScore();
        this.rank = leaderboard.getRank();
        this.userName = leaderboard.getUser() != null ? leaderboard.getUser().getName() : null;
    }
}

