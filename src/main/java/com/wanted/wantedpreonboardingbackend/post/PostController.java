package com.wanted.wantedpreonboardingbackend.post;

import com.wanted.wantedpreonboardingbackend.common.LoginCheck;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostCreateRequest;
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
}
