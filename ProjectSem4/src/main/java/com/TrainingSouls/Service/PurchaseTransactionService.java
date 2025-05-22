package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.PurchaseTransactionResponse;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.PurchaseTransaction;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Mapper.PurchaseTransactionMapper;
import com.TrainingSouls.Repository.PurchaseTransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseTransactionService {
    PurchaseTransactionRepository purchaseTransactionRepository;
    PurchaseTransactionMapper mapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<PurchaseTransactionResponse> getAllPurchaseTransactions() {
        return purchaseTransactionRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PurchaseTransaction getPurchaseTransactionEntityById(int id) {
        return purchaseTransactionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }


    public PurchaseTransactionResponse getPurchaseTransactionById(int id) {
        PurchaseTransaction purchaseTransaction = purchaseTransactionRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        return mapper.toDto(purchaseTransaction);
    }

    public PurchaseTransaction createPurchaseTransaction(User user, StoreItem item, PurchaseTransaction.PaymentMethod paymentMethod, PointsTransaction.TransactionStatus status) {
        PurchaseTransaction transaction = new PurchaseTransaction();
        transaction.setUser(user);
        transaction.setItem(item);
        transaction.setAmount(item.getPrice());
        transaction.setPaymentMethod(paymentMethod);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setStatus(status);
        return purchaseTransactionRepository.save(transaction);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deletePurchaseTransaction(int id) {
        purchaseTransactionRepository.deleteById(id);
    }
}
