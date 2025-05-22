package com.TrainingSouls.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CoachResponseDTO {
    private Long userID;
    private String name;
    private String email;

}

