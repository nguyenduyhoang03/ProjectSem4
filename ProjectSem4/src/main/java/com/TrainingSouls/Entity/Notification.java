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
@Table(name = "notifications")
public class Notification {
    @Id
    @Column(name = "NotificationID", nullable = false)
    private Integer NotificationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Lob
    @Column(name = "Content", nullable = false)
    private String content;

    @Lob
    @Column(name = "Type", nullable = false)
    private String type;

    @ColumnDefault("current_timestamp()")
    @Column(name = "Date")
    private Instant date;

}