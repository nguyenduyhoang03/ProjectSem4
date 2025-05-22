package com.TrainingSouls.DTO.Request;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String gender;
    @NotBlank
    private int age;
    @NotBlank
    private double height;
    @NotBlank
    private double weight;
    @NotBlank
    private String activityLevel;
    @NotBlank
    private String fitnessGoal;
    @NotBlank
    private String level;

    private List<String> medicalConditions;
    private Integer strength = 1;
    private Integer endurance = 1;
    private Integer health = 1;
    private Integer agility = 1;
    private Integer deathPoints = 0;
}
