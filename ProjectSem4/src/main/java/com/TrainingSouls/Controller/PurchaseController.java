package com.TrainingSouls.Controller;

import com.TrainingSouls.Service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
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
        return purchaseService.purchaseItem(request, itemId);
    }
}