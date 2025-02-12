package com.TrainingSouls.DTO.Response;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private Integer id;

    private String name;

    private String email;

    private String accountType;

    private Integer points;

    private Integer level;

    private Set<String> roles;
}
