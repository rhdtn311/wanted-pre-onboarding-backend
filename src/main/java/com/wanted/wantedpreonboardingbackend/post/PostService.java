package com.wanted.wantedpreonboardingbackend.post;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostsResponse;
import com.wanted.wantedpreonboardingbackend.user.UserRepository;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import com.wanted.wantedpreonboardingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createPost(String title, String content, Email email) {
        User user = getUserOrException(email);
        Post post = new Post(title, content, user);
        postRepository.save(post);

        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostsResponse getPosts(Pageable pageable) {
        List<Post> posts = postRepository.findPostsFetchWithUser(pageable);

        return PostsResponse.of(posts);
    }

    private User getUserOrException(Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
