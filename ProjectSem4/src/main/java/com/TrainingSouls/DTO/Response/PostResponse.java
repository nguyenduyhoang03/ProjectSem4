package com.TrainingSouls.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private Long userID;
    private String name;
    private String title;
    private List<String> imgUrl;
    private List<String> videoUrl;
    private List<String> content;
    private LocalDateTime createdAt;
}
