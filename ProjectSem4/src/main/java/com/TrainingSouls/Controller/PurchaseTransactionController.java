package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.PurchaseTransactionResponse;
import com.TrainingSouls.DTO.Response.PointsTransactionDTO;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.PurchaseTransaction;
import com.TrainingSouls.Service.PointsTransactionService;
import com.TrainingSouls.Service.PurchaseTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/PurchaseTransaction")
@RequiredArgsConstructor
public class PurchaseTransactionController {

    private final PurchaseTransactionService purchaseTransactionService;

    @GetMapping
    public List<PurchaseTransactionResponse> getAllPurchaseTransactions() {
        return purchaseTransactionService.getAllPurchaseTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseTransactionResponse> getPurchaseTransactionById(@PathVariable int id) {
        PurchaseTransactionResponse purchaseTransaction = purchaseTransactionService.getPurchaseTransactionById(id);
        return ResponseEntity.ok(purchaseTransaction);
    }

    @DeleteMapping("/{id}")
    public String deletePurchaseTransactionById(@PathVariable int id) {
        purchaseTransactionService.deletePurchaseTransaction(id);
        return "Xoa giao dich thanh cong";
    }


}
