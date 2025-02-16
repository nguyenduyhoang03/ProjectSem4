package com.TrainingSouls.DTO.Request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreation {
    private Long userId;
    private String title;
    private List<String> imgUrl;
    private List<String> videoUrl;
    private String content;
    private LocalDateTime createdAt;
}
