package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.PostCreation;
import com.TrainingSouls.DTO.Request.PostUpdate;
import com.TrainingSouls.DTO.Response.PostResponse;
import com.TrainingSouls.Entity.Post;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Mapper.PostMapper;
import com.TrainingSouls.Repository.PostRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public PostResponse createPost(PostCreation request , HttpServletRequest httpServletRequest) {
        //lay userID tu Token
        Long userId = JWTUtils.getSubjectFromRequest(httpServletRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // Lấy User từ DB


        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .imgUrl(request.getImgUrl())
                .videoUrl(request.getVideoUrl())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);

        return postMapper.toPostResponse(post);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public Post updatePost(Long postId, PostUpdate request) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
            post.setTitle(request.getTitle());
            post.setImgUrl(request.getImgUrl());
            post.setVideoUrl(request.getVideoUrl());
            post.setContent(request.getContent());

        return postRepository.save(post);
    }



    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));

        return postMapper.toPostResponse(post);
    }


    public List<Post> getAllPosts() {
        List<Post> post = postRepository.findAll();

        return post.stream().toList();
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public String DeletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
        return "Deleted";
    }
}
