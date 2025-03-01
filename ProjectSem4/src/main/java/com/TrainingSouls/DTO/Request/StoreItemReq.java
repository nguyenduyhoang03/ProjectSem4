package com.TrainingSouls.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @Min(value = 0, message = "quantity cannot be negative")
    Integer quantity;

    String description;
}
