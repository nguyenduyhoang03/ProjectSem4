package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "UserProfile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "UserID", nullable = false)
    private User user;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;  //ti le mo

    @Column(name = "muscle_mass_percentage")
    private Double muscleMassPercentage; //ti le co bap

    @Column(name = "activity_level")
    private String activityLevel; //cuong do hoat dong

    @Column(name = "fitness_goal")
    private String fitnessGoal; //muc tieu

    @Column(name = "level")
    private String level; //benginer,....

    @ElementCollection
    @CollectionTable(name = "UserMedicalConditions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "medical_condition")
    private List<String> medicalConditions = new ArrayList<>();


    public void calculateMetrics() {
        this.bmi = weight / ((height / 100) * (height / 100));
        this.bodyFatPercentage = calculateBodyFat();
        this.muscleMassPercentage = 100 - bodyFatPercentage;
    }

    private Double calculateBodyFat() {
        if (gender.equalsIgnoreCase("male")) {
            return (1.2 * bmi) + (0.23 * age) - 16.2;
        } else {
            return (1.2 * bmi) + (0.23 * age) - 5.4;
        }
    }
}

