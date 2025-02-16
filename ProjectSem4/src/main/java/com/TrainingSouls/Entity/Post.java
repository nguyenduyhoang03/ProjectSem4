package com.TrainingSouls.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String title;
    @ElementCollection
    private List<String> imgUrl;
    @ElementCollection
    private List<String> videoUrl;
    private String content;
    private LocalDateTime createdAt;
}
