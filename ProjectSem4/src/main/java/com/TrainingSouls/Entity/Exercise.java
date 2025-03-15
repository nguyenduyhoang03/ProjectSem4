package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "img")
    private String img;

    @Column(name = "icon")
    private String icon;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private int duration; // Thời gian tập (phút)

    @Column(name = "reps")
    private String reps; // Số lần lặp

    @Column(name = "distance")
    private Double distance;
}

