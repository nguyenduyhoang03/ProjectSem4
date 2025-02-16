package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.PostCreation;
import com.TrainingSouls.Entity.Post;
import com.TrainingSouls.Repository.PostRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(PostCreation request , HttpServletRequest httpServletRequest) {
        //lay userID tu Token
        String userId = JWTUtils.getSubjectFromRequest(httpServletRequest);

        Post post = Post.builder()
                .userId(Long.valueOf(userId))
                .title(request.getTitle())
                .imgUrl(request.getImgUrl())
                .videoUrl(request.getVideoUrl())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public String DeletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
        return "Deleted";
    }
}
