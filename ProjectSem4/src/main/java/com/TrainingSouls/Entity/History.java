package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "history")
public class History {
    @Id
    @Column(name = "HistoryId", nullable = false)
    private Integer HistoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", nullable = false)
    private User userID;

    @Column(name = "Activity", nullable = false)
    private String activity;

    @ColumnDefault("current_timestamp()")
    @Column(name = "Date")
    private Instant date;

}