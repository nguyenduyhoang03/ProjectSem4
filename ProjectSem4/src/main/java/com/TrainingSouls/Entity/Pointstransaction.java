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
@Table(name = "pointstransactions")
public class Pointstransaction {
    @Id
    @Column(name = "TransactionID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Lob
    @Column(name = "Type", nullable = false)
    private String type;

    @Column(name = "Points", nullable = false)
    private Integer points;

    @ColumnDefault("current_timestamp()")
    @Column(name = "Date")
    private Instant date;

    @Lob
    @Column(name = "Description")
    private String description;

}