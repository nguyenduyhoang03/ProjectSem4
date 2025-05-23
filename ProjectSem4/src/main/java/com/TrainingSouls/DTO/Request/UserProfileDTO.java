package com.TrainingSouls.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {

    @NotBlank(message = "Giới tính không được để trống")
    private String gender;

    @Min(value = 1, message = "Tuổi phải lớn hơn 0")
    @Max(value = 120, message = "Tuổi phải nhỏ hơn hoặc bằng 120")
    private int age;

    @Positive(message = "Chiều cao phải là số dương")
    private double height;

    @Positive(message = "Cân nặng phải là số dương")
    private double weight;

    @NotBlank(message = "Mức độ hoạt động không được để trống")
    private String activityLevel;

    @NotBlank(message = "Mục tiêu tập luyện không được để trống")
    private String fitnessGoal;

    @NotBlank(message = "Cấp độ không được để trống")
    private String level;

    private List<String> medicalConditions;

    @Min(value = 1, message = "Strength phải >= 1")
    private Integer strength = 1;

    @Min(value = 1, message = "Endurance phải >= 1")
    private Integer endurance = 1;

    @Min(value = 1, message = "Health phải >= 1")
    private Integer health = 1;

    @Min(value = 1, message = "Agility phải >= 1")
    private Integer agility = 1;

    @Min(value = 0, message = "DeathPoints không được âm")
    private Integer deathPoints = 0;
}

