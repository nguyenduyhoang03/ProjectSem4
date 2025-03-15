package com.TrainingSouls.DTO.Response;

import com.TrainingSouls.Entity.Role;
import com.TrainingSouls.Entity.UserItem;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserResponse {
    private Integer id;

    private String name;

    private String email;

    private String accountType;

    private Integer points;

    private Integer level;

    private Set<RoleResponse> roles;

    private List<UserItem> purchasedItems = new ArrayList<>();
}
