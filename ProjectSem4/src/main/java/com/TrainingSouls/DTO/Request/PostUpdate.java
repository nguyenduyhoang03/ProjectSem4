package com.TrainingSouls.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostUpdate {
     String title;
     List<String> imgUrl;
     List<String> videoUrl;
     List<String> content;

}
