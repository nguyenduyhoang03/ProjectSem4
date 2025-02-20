package com.TrainingSouls.DTO.Request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationReq {
    @Size(min = 6, max = 50, message = "NAME_INVALID")
    private String name;
    private String email;
    @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
    private String password;


}
