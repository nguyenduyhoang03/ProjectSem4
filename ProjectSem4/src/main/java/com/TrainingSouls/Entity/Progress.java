package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "progress")
public class Progress {
    @Id
    @Column(name = "ProgressId", nullable = false)
    private Integer ProgressId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "WorkoutId", nullable = false)
    private Workout workoutId;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "Strength")
    private Integer strength;

    @Column(name = "Endurance")
    private Integer endurance;

    @Column(name = "Health")
    private Integer health;

    @Column(name = "Agility")
    private Integer agility;

}