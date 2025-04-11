package com.TrainingSouls.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_items")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Dùng chung 1 bảng
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
public class UserItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "accountType", "level", "roles"})
    @JsonBackReference
    private User user;

    private Integer itemId;

    private LocalDateTime purchasedAt = LocalDateTime.now();

    @Column(name = "expiration_date")
    private LocalDate expirationDate;
}

