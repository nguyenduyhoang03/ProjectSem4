package com.TrainingSouls.DTO.Request;

import com.TrainingSouls.DTO.Response.StoreItemDTO;
import com.TrainingSouls.DTO.Response.UserDTO;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.PurchaseTransaction;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseTransactionResponse {
    private UserDTO user;
    private StoreItemDTO item;
    private BigDecimal amount;
    private PurchaseTransaction.PaymentMethod paymentMethod;
    private PointsTransaction.TransactionStatus status;
    private LocalDateTime transactionTime;
}
