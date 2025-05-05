package com.TrainingSouls.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasedItemResponse {
    private Long itemId;
    private String name;
    private LocalDate purchasedAt;
    private LocalDate expirationDate;
}
