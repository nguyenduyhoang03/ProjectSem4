package com.TrainingSouls.DTO.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CoachStudentsDTO {
    private Long userID;
    private String name;
    private String email;
    private String level;

}

