package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.PostCreation;
import com.TrainingSouls.Entity.Post;
import com.TrainingSouls.Enums.PostType;
import com.TrainingSouls.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Long userId, PostCreation request) {
        Post post = Post.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .ImgUrl(request.getImgUrl())
                .content(request.getContent())
                .type(PostType.POST)
                .createdAt(LocalDateTime.now())
                .build();
        return postRepository.save(post);
    }

    public Post addComment(Long userId, Long postId, String content) {
        Post comment = Post.builder()
                .userId(userId)
                .content(content)
                .parentId(postId)
                .type(PostType.COMMENT)
                .createdAt(LocalDateTime.now())
                .build();
        return postRepository.save(comment);
    }

    public Post addLike(Long userId, Long postId) {
        Post like = Post.builder()
                .userId(userId)
                .parentId(postId)
                .type(PostType.LIKE)
                .createdAt(LocalDateTime.now())
                .build();
        return postRepository.save(like);
    }

    public List<Post> getAllPosts() {
        return postRepository.findByType(PostType.POST);
    }

    public List<Post> getComments(Long postId) {
        return postRepository.findByParentId(postId);
    }

    public List<Post> getLikes(Long postId) {
        return postRepository.findByParentId(postId);
    }
    public String DeletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
        return "Deleted";
    }
}
