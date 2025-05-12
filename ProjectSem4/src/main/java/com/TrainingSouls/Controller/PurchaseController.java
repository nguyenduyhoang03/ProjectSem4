package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.PurchaseRequest;
import com.TrainingSouls.DTO.Response.ResponseObject;
import com.TrainingSouls.Service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/{itemId}")
    public String buyItem(HttpServletRequest request, @PathVariable Integer itemId) {
        return purchaseService.purchaseItemByPoints(request, itemId);
    }

    @PostMapping("/complete")
    public ResponseEntity<ResponseObject<String>> completePaypalPurchase(
            HttpServletRequest request,
            @RequestBody PurchaseRequest purchaseRequest) {

        purchaseService.completePaypalPurchase(request, purchaseRequest);
        return ResponseEntity.ok(new ResponseObject<>(HttpStatus.OK, "Purchase completed successfully", null));
    }

    @PostMapping("/stripeCompleted")
    public ResponseEntity<ResponseObject<String>> completeStripePurchase(HttpServletRequest request, @RequestBody PurchaseRequest purchaseRequest) {
        purchaseService.completeStripePurchase(request, purchaseRequest);
        return ResponseEntity.ok(new ResponseObject<>(HttpStatus.OK, "Purchase completed successfully", null));
    }



    @DeleteMapping("/deleteExpiredItems")
    public void deleteExpiredItems(HttpServletRequest request) {
        purchaseService.deleteExpiredItems();
    }
}