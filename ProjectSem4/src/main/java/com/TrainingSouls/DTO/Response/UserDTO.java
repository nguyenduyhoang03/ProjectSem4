package com.TrainingSouls.DTO.Response;

import com.TrainingSouls.Entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long userID;
    private String name;
    private String email;
    private Integer points;

    public UserDTO(User user) {
        if (user != null) {
            this.userID = user.getUserID();
            this.name = user.getName();
            this.email = user.getEmail();
            this.points = user.getPoints();
        }
    }


}

