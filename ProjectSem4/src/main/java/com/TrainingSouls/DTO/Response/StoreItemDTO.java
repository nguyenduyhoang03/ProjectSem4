package com.TrainingSouls.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreItemDTO {
    private Integer itemId;
    private String name;
    private BigDecimal price;
}


