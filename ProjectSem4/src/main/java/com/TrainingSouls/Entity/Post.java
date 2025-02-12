package com.TrainingSouls.Entity;

import com.TrainingSouls.Enums.PostType;
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

    private Long userId; // ID của người dùng tạo bài viết/bình luận/like
    private String title;
    @ElementCollection
    private List<String> ImgUrl;
    private String content; // Nội dung bài viết hoặc bình luận
    private Long parentId; // ID của bài viết cha (nếu là bình luận hoặc like)

    @Enumerated(EnumType.STRING)
    private PostType type; // Loại: POST, COMMENT, LIKE

    private LocalDateTime createdAt;
}
