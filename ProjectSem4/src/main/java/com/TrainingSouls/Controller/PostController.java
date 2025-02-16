package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.PostCreation;
import com.TrainingSouls.Entity.Post;
import com.TrainingSouls.Service.PostService;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(@RequestBody PostCreation request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(postService.createPost(request, httpServletRequest));
    }

    @GetMapping("/getAllPost")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }


    @DeleteMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable Long postId) {
        return postService.DeletePost(postId);
    }
}
