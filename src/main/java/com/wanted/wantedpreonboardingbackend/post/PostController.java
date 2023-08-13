package com.wanted.wantedpreonboardingbackend.post;

import com.wanted.wantedpreonboardingbackend.common.LoginCheck;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostCreateRequest;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostResponse;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostUpdateRequest;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostsResponse;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(
            @RequestBody PostCreateRequest postCreateRequest,
            @LoginCheck Email email
    ) {
        Long postId = postService.createPost(postCreateRequest.title(), postCreateRequest.content(), email);

        return ResponseEntity.created(
                URI.create("/posts/" + postId)
        ).build();
    }

    @GetMapping
    public ResponseEntity<PostsResponse> getPosts(Pageable pageable) {
        PostsResponse postsResponse = postService.getPosts(pageable);

        return ResponseEntity.ok(postsResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse postResponse = postService.getPost(postId);

        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest postUpdateRequest,
            @LoginCheck Email email
    ) {
        postService.updatePost(email, postId, postUpdateRequest.title(), postUpdateRequest.content());

        return ResponseEntity.noContent().build();
    }
}
