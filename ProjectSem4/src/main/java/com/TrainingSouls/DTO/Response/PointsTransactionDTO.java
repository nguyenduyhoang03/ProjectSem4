package com.TrainingSouls.DTO.Response;

import com.TrainingSouls.Entity.PointsTransaction;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PointsTransactionDTO {
    private Integer transactionId;
    private UserDTO user;
    private PointsTransaction.TransactionType type;
    private Integer points;
    private Instant date;
    private String description;
    private PointsTransaction.TransactionStatus status;

    public PointsTransactionDTO(PointsTransaction transaction) {
        this.transactionId = transaction.getTransactionID();
        this.user = new UserDTO(transaction.getUser());
        this.type = transaction.getType();
        this.points = transaction.getPoints();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
        this.status = transaction.getStatus();
    }
}

