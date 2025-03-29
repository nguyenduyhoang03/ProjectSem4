package com.TrainingSouls.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String gender;
    private int age;
    private double height;
    private double weight;
    private String activityLevel;
    private String fitnessGoal;
    private String level;
    private List<String> medicalConditions;
    private Integer strength = 1;
    private Integer Endurance = 1;
    private Integer Health = 1;
    private Integer Agility = 1;
    private Integer deathPoints = 0;
}
