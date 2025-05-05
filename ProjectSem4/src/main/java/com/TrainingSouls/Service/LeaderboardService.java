package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Response.LeaderboardDTO;
import com.TrainingSouls.Entity.Leaderboard;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Repository.LeaderboardRepository;
import com.TrainingSouls.Repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;
    private final UserProfileRepository userProfileRepository;

    public ResponseEntity<List<LeaderboardDTO>> getAllLeaderboard() {
        List<Leaderboard> leaders = leaderboardRepository.findAll();

        List<LeaderboardDTO> result = leaders.stream()
                .map(LeaderboardDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);

    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void updateLeaderboard() {
        List<UserProfile> profiles = userProfileRepository.findAll();
        List<Leaderboard> updateRanks = new ArrayList<>();

        for (UserProfile profile : profiles) {
            User user = profile.getUser();

            int strength = profile.getStrength();
            int agility = profile.getAgility();
            int endurance = profile.getEndurance();
            int health = profile.getHealth();
            int deathPoints = profile.getDeathPoints();

            //tinh diem
            double totalScore = (strength * 1.2 + endurance * 1.2 + agility + health) - (deathPoints);
            totalScore = Math.max(0, totalScore);


            Leaderboard leaderboard = leaderboardRepository.findByUser(user).orElse(new Leaderboard());
            leaderboard.setUser(user);
            leaderboard.setStrengthScore(strength);
            leaderboard.setAgilityScore(agility);
            leaderboard.setEnduranceScore(endurance);
            leaderboard.setHealthScore(health);
            leaderboard.setDeathpoints(deathPoints);
            leaderboard.setTotalScore(totalScore);

            updateRanks.add(leaderboard);
        }

        leaderboardRepository.saveAll(updateRanks);

        // Gán rank theo thứ tự điểm từ cao đến thấp
        List<Leaderboard> ranked = leaderboardRepository.findAllByOrderByTotalScoreDesc();
        for (int i = 0; i < ranked.size(); i++) {
            ranked.get(i).setRank(i + 1);
        }
        leaderboardRepository.saveAll(ranked);

    }
}
