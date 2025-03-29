package com.TrainingSouls.DTO.Request;


import com.TrainingSouls.Entity.Role;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdate {
    private String name;
    private Set<String> roles;
}
