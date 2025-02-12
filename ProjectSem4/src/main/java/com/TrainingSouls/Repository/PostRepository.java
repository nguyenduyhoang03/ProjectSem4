package com.TrainingSouls.Repository;

import com.TrainingSouls.Entity.Post;
import com.TrainingSouls.Enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByType(PostType type); // Lấy bài viết, bình luận hoặc like theo loại
    List<Post> findByParentId(Long parentId); // Lấy bình luận hoặc lượt thích của một bài viết
}
