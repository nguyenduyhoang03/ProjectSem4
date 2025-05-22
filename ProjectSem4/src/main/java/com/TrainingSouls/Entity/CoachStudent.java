package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coach_student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private User student;
}


