package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "UserID", nullable = false)
     Long userID;

    @Column(name = "Name", nullable = false)
     String name;

    @Column(name = "Email", nullable = false)
     String email;

    @Column(name = "Password", nullable = false)
     String password;

    @ColumnDefault("'Basic'")
    @Lob
    @Column(name = "AccountType")
     String accountType;

    @ColumnDefault("0")
    @Column(name = "Points")
     Integer points;

    @ColumnDefault("1")
    @Column(name = "Level")
     Integer level;

    @ManyToMany
    Set<Role> roles;

    @PrePersist
    public void generateId() {
        if (this.userID == null) {
            this.userID = (long) Math.toIntExact(Math.abs(UUID.randomUUID().getMostSignificantBits() % 900000000) + 100000000);
            // ID sẽ có 9 chữ số từ 100000000 - 999999999
        }
    }


}