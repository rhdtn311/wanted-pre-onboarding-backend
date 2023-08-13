package com.wanted.wantedpreonboardingbackend.post;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostResponse;
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

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = getPostOrException(postId);

        return PostResponse.of(post);
    }

    @Transactional
    public void updatePost(Email email, Long postId, String title, String content) {
        Post post = getPostOrException(postId);
        User user = getUserOrException(email);
        validatePostAccessAuthority(post, user);

        post.update(title, content);
    }

    private User getUserOrException(Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private Post getPostOrException(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private void validatePostAccessAuthority(Post post, User user) {
        if (post.isNotWrittenBy(user)) {
            throw new IllegalArgumentException("게시글 접근 권한이 없는 사용자입니다.");
        }
    }
}
