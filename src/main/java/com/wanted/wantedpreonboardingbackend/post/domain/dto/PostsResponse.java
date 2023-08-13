package com.wanted.wantedpreonboardingbackend.post.domain.dto;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;

import java.util.List;

public record PostsResponse(List<PostResponse> postResponses) {
    record PostResponse(String title, String content, Email email) {
    }

    public static PostsResponse of(List<Post> posts) {
        return new PostsResponse(
                posts.stream()
                        .map(post -> new PostResponse(post.getTitle(), post.getContent(), post.getUser().getEmail()))
                        .toList()
        );
    }
}
