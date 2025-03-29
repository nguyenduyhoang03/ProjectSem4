package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "rewards")
public class Reward {
    @Id
    @Column(name = "RewardId", nullable = false)
    private Integer RewardId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "PointsRequired")
    private Integer pointsRequired;

    @Column(name = "Price", precision = 10, scale = 2)
    private BigDecimal price;

    @Lob
    @Column(name = "Type", nullable = false)
    private String type;

    @Column(name = "DurationMonths")
    private Integer durationMonths;

}