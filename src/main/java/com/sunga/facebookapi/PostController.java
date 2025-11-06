package com.sunga.facebookapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {"http://localhost:5173", "https://facebook-ui-joa2.onrender.com"})
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            Post post = existingPost.get();
            post.setAuthor(postDetails.getAuthor());
            post.setContent(postDetails.getContent());
            post.setImageUrl(postDetails.getImageUrl());
            return ResponseEntity.ok(postRepository.save(post));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
