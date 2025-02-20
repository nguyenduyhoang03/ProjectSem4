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
@Table(name = "healthdata")
public class Healthdatum {
    @Id
    @Column(name = "HealthDataID", nullable = false)
    private Integer HealthDataID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Lob
    @Column(name = "HealthMetrics", nullable = false)
    private String healthMetrics;

}