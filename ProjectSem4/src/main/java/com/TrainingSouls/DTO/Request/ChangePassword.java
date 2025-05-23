package com.TrainingSouls.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePassword {
    @NotBlank
    String oldPassword;
    @NotBlank
    String newPassword;
}
