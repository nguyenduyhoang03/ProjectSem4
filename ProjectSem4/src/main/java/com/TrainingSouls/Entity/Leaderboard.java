package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "leaderboard")
public class Leaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "UserID", nullable = false, unique = true)
    private User user;

    @Column(name = "strength_score", nullable = false)
    private int strengthScore;

    @Column(name = "endurance_score", nullable = false)
    private int enduranceScore;

    @Column(name = "health_score", nullable = false)
    private int healthScore;

    @Column(name = "agility_score", nullable = false)
    private int agilityScore;

    @Column(name = "deathpoints", nullable = false)
    private int deathpoints;

    @Column(name = "total_score", nullable = false)
    private Double totalScore;

    @Column(name = "user_rank", nullable = false)
    private int rank;

}