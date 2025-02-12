package com.TrainingSouls.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnumTable {
    @Lob
    @Column(name = "leaderboard_Rank_enum")
    private String leaderboardRankEnum;

    @Lob
    @Column(name = "notifications_Type_enum")
    private String notificationsTypeEnum;

    @Lob
    @Column(name = "pointstransactions_Type_enum")
    private String pointstransactionsTypeEnum;

    @Lob
    @Column(name = "rewards_Type_enum")
    private String rewardsTypeEnum;

    @Lob
    @Column(name = "users_AccountType_enum")
    private String usersAccounttypeEnum;

    @Lob
    @Column(name = "vipsubscriptions_Status_enum")
    private String vipsubscriptionsStatusEnum;

    @Lob
    @Column(name = "workouts_Type_enum")
    private String workoutsTypeEnum;

    @Lob
    @Column(name = "workouts_Difficulty_enum")
    private String workoutsDifficultyEnum;

}