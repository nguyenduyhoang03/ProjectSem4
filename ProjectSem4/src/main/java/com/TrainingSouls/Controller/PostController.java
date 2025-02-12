package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.PostCreation;
import com.TrainingSouls.Entity.Post;
import com.TrainingSouls.Service.PostService;
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
    public Post createPost(@RequestParam Long userId, @RequestBody PostCreation request) {
        return postService.createPost(userId, request);
    }

    @PostMapping("/{postId}/comments")
    public Post addComment(@RequestParam Long userId, @PathVariable Long postId, @RequestBody String content) {
        return postService.addComment(userId, postId, content);
    }

    @PostMapping("/{postId}/likes")
    public Post addLike(@RequestParam Long userId, @PathVariable Long postId) {
        return postService.addLike(userId, postId);
    }

    @GetMapping("/getAllPost")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}/comments")
    public List<Post> getComments(@PathVariable Long postId) {
        return postService.getComments(postId);
    }

    @GetMapping("/{postId}/likes")
    public List<Post> getLikes(@PathVariable Long postId) {
        return postService.getLikes(postId);
    }

    @DeleteMapping("/delete-post")
    public void deletePost(@RequestParam Long postId) {
        postService.DeletePost(postId);
    }
}
