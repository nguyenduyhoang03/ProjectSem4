package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "customworkouts")
public class Customworkout {
    @Id
    @Column(name = "CustomWorkoutID", nullable = false)
    private Integer CustomWorkoutID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Column(name = "Name", nullable = false)
    private String name;

    @Lob
    @Column(name = "Description")
    private String description;

    @Lob
    @Column(name = "Goal")
    private String goal;

}