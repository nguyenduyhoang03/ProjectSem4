package com.TrainingSouls.DTO.Request;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdate {
    private String name;
    private String password;
    List<String> roles;
}
