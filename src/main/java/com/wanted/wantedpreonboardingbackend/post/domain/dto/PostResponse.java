package com.wanted.wantedpreonboardingbackend.post.domain.dto;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;

public record PostResponse(Long id, String title, String content, Email email) {
    public static PostResponse of(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getUser().getEmail());
    }
}
