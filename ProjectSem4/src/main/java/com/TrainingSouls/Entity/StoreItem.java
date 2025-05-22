package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "storeitems")
public class StoreItem {
    @Id
    @Column(name = "ItemID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "PointsRequired", nullable = false)
    private Integer pointsRequired;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "DurationInDays")
    private Integer durationInDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "ItemType", nullable = false)
    private StoreItemType itemType;

    public enum StoreItemType {
        SUBSCRIPTION,
        AVATAR
    }
}