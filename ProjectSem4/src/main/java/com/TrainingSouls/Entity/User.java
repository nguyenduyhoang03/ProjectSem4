package com.TrainingSouls.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "AccountType")
     String accountType = "Basic";

    @ColumnDefault("0")
    @Column(name = "Points")
     Integer points = 0;

    @ColumnDefault("1")
    @Column(name = "Level")
     Integer level = 1;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UserItem> purchasedItems = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private UserProfile userProfile;

    @PrePersist
    public void generateId() {
        if (this.userID == null) {
            this.userID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        }
    }


}