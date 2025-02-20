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
    @Column(name = "ProgressID", nullable = false)
    private Integer ProgressID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "WorkoutID", nullable = false)
    private Workout workoutID;

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