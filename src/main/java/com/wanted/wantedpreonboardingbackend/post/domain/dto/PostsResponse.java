package com.wanted.wantedpreonboardingbackend.post.domain.dto;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;

import java.util.List;

public record PostsResponse(List<PostResponse> postResponses) {
    record PostResponse(Long id, String title, String content, Email email) {
    }

    public static PostsResponse of(List<Post> posts) {
        return new PostsResponse(
                posts.stream()
                        .map(post -> new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getUser().getEmail()))
                        .toList()
        );
    }
}
