package com.TrainingSouls.DTO.Response;

import com.TrainingSouls.Entity.Role;
import com.TrainingSouls.Entity.UserItem;
import com.TrainingSouls.Entity.UserProfile;
import lombok.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithScoreResponse {
    private Long userID;
    private String name;
    private String email;
    private String accountType;
    private Integer points;
    private Integer level;
    private Integer streak;
    private Set<Role> roles;
    private List<UserItem> purchasedItems;
    private UserProfile userProfile;
    private Double totalScore; // từ Leaderboard
}

