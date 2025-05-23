package com.TrainingSouls.DTO.Request;

import com.TrainingSouls.Entity.StoreItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreItemReq {
    @Size(min = 5, max = 150, message = "name must be at least 5 characters and at most 150 characters")
    String name;

    @Min(value = 1, message = "point must be at least 1")
    Integer pointsRequired;

    @Min(value = 1, message = "quantity cannot be negative, and must be least 1")
    Integer quantity;

    String description;

    @Min(value = 1, message = "durationInDays must be at least 1")
    Integer durationInDays;

    @NotNull(message = "itemType cannot be null")
    StoreItem.StoreItemType itemType;

    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    BigDecimal price;
}

