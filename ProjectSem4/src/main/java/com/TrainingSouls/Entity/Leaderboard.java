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
    @Column(name = "UserID", nullable = false)
    private Integer UserID;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User users;

    @ColumnDefault("0")
    @Column(name = "LifePoints")
    private Integer lifePoints;

    @ColumnDefault("0")
    @Column(name = "Power")
    private Integer power;

    @ColumnDefault("'Bronze'")
    @Lob
    @Column(name = "Rank")
    private String rank;

}