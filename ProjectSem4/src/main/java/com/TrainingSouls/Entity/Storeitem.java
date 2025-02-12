package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storeitems")
public class Storeitem {
    @Id
    @Column(name = "ItemID", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "PointsRequired", nullable = false)
    private Integer pointsRequired;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Lob
    @Column(name = "Description")
    private String description;

}