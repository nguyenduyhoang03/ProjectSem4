package com.TrainingSouls.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PointsTransaction {
    @Id
    @Column(name = "TransactionID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer TransactionID;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    @JsonIgnoreProperties({"password", "accountType", "level", "roles"}) // Ẩn các trường không cần thiết
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private TransactionType type;

    @Column(name = "Points", nullable = false)
    private Integer points;

    @ColumnDefault("current_timestamp()")
    @Column(name = "Date")
    private Instant date;

    @Lob
    @Column(name = "Description")
    private String description;

    @Enumerated(EnumType.STRING) // Thêm trạng thái giao dịch
    @Column(name = "Status", nullable = false)
    private TransactionStatus status;

    public enum TransactionType {
        EARN, SPEND
    }

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED
    }

}