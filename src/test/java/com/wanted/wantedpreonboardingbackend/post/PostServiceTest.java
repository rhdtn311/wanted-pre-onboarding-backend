package com.wanted.wantedpreonboardingbackend.post;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostResponse;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostUpdateRequest;
import com.wanted.wantedpreonboardingbackend.post.domain.dto.PostsResponse;
import com.wanted.wantedpreonboardingbackend.user.UserRepository;
import com.wanted.wantedpreonboardingbackend.user.UserService;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import com.wanted.wantedpreonboardingbackend.user.domain.Password;
import com.wanted.wantedpreonboardingbackend.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 생성에 성공한다.")
    void createPostSuccess() {
        // given
        Email email = new Email("email@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        Post post = new Post("title", "content", user);

        // when
        Long postId = postService.createPost(post.getTitle(), post.getContent(), email);

        // then
        Optional<Post> postOpt = postRepository.findById(postId);
        assertThat(postOpt).isPresent();
    }

    @Test
    @DisplayName("사용자가 존재하지 않아 게시글 생성에 실패한다.")
    void createPostFailDueToNotExistsUser() {
        // given
        Email email = new Email("email@wanted.com");
        Email differentEmail = new Email("differentEmail@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        Post post = new Post("title", "content", user);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> postService.createPost(post.getTitle(), post.getContent(), differentEmail));
    }

    @Test
    @DisplayName("게시글 목록 조회에 성공한다.")
    void getPostsSuccess() {
        // given
        Email email = new Email("email@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            Post post = new Post("title" + i, "content" + i, user);
            postRepository.save(post);
        }

        Pageable pageable = PageRequest.of(0, 5);

        // when, then
        PostsResponse postsResponse = postService.getPosts(pageable);

        // then
        assertThat(postsResponse.postResponses()).hasSize(5);
    }

    @Test
    @DisplayName("특정 게시글 조회에 성공한다.")
    void getPostSuccess() {
        // given
        Email email = new Email("email@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        Post post = new Post("title", "content", user);
        postRepository.save(post);

        // when
        PostResponse postResponse = postService.getPost(post.getId());

        // then
        assertThat(postResponse).usingRecursiveComparison()
                .comparingOnlyFields("title", "content")
                .isEqualTo(post);
    }

    @Test
    @DisplayName("특정 게시글 수정에 성공한다.")
    void updatePostSuccess() {
        // given
        Email email = new Email("email@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        Post post = new Post("title", "content", user);
        postRepository.save(post);

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("update title", "update content");

        // when
        postService.updatePost(email, post.getId(), postUpdateRequest.title(), postUpdateRequest.content());

        // then
        assertThat(postUpdateRequest).usingRecursiveComparison()
                .comparingOnlyFields("title", "content")
                .isEqualTo(post);
    }

    @Test
    @DisplayName("게시글이 존재하지 않아 특정 게시글 수정에 실패한다.")
    void updatePostFailDueToNotExistPost() {
        // given
        Email email = new Email("email@wanted.com");
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("update title", "update content");

        // when, then
        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(email, 0L, postUpdateRequest.title(), postUpdateRequest.content()));
    }

    @Test
    @DisplayName("게시글 수정 권한이 없어 특정 게시글 수정에 실패한다.")
    void updatePostFailDueToNoAuthority() {
        // given
        Email email = new Email("email@wanted.com");
        Email anotherEmail = new Email("anotherEmail@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        Post post = new Post("title", "content", user);
        postRepository.save(post);

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("update title", "update content");

        // when, then
        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(anotherEmail, post.getId(), postUpdateRequest.title(), postUpdateRequest.content()));
    }

    @Test
    @DisplayName("특정 게시글 삭제에 성공한다.")
    void deletePostSuccess() {
        // given
        Email email = new Email("email@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        Post post = new Post("title", "content", user);
        postRepository.save(post);

        // when
        postService.deletePost(email, post.getId());

        // then
        Optional<Post> postOpt = postRepository.findById(post.getId());
        assertThat(postOpt).isEmpty();

    }

    @Test
    @DisplayName("게시글이 존재하지 않아 특정 게시글 삭제에 실패한다.")
    void deletePostFailDueToNotExistPost() {
        // given
        Email email = new Email("email@wanted.com");

        // when, then
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(email, 0L));
    }

    @Test
    @DisplayName("게시글 삭제 권한이 없어 특정 게시글 수정에 실패한다.")
    void deletePostFailDueToNoAuthority() {
        // given
        Email email = new Email("email@wanted.com");
        Email anotherEmail = new Email("anotherEmail@wanted.com");
        Password password = new Password("validPassword");
        User user = new User(email, password);
        userRepository.save(user);

        Post post = new Post("title", "content", user);
        postRepository.save(post);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(anotherEmail, post.getId()));
    }
}