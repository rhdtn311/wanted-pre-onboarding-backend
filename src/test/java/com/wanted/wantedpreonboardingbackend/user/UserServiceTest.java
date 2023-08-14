package com.wanted.wantedpreonboardingbackend.user;

import com.wanted.wantedpreonboardingbackend.common.PasswordEncoder;
import com.wanted.wantedpreonboardingbackend.jwt.JwtTokenManager;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import com.wanted.wantedpreonboardingbackend.user.domain.Password;
import com.wanted.wantedpreonboardingbackend.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenManager tokenManager;

    @Test
    @DisplayName("회원가입에 성공한다.")
    void joinSuccess() {
        // given
        String passwordValue = "validPassword";
        Email email = new Email("email@wanted.com");
        Password password = new Password(passwordValue);

        // when
        userService.join(email, password);

        // then
        Optional<User> userOpt = userRepository.findByEmail(email);

        assertThat(userOpt).isPresent();
        assertThat(userOpt.get().getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.isMatch(passwordValue, password.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입시 이메일 중복으로 실패한다.")
    void joinFailDueToDuplicatedEmail() {
        // given
        String emailAddress = "duplicatedEmail@wanted.com";
        Email originalEmail = new Email(emailAddress);
        Email duplicatedEmail = new Email(emailAddress);
        Password password = new Password("validPassword");

        userRepository.save(new User(originalEmail, password));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> userService.join(duplicatedEmail, password));
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void loginSuccess() {
        // gvien
        String originalPassword = "validPassword";
        Email email = new Email("email@wanted.com");
        Password password = new Password(originalPassword);
        String encryptedPassword = passwordEncoder.encrypt(password.getPassword());
        password.changeEncryptedPassword(encryptedPassword);

        User user = new User(email, password);

        userRepository.save(user);

        // when
        String token = userService.login(email, new Password(originalPassword));

        // then
        assertDoesNotThrow(() -> tokenManager.getUserEmailFromToken(token));
    }

    @Test
    @DisplayName("사용자가 존재하지 않아 로그인에 실패한다.")
    void loginFailDueToNotExistUser() {
        // gvien
        String originalPassword = "validPassword";
        Email email = new Email("email@wanted.com");

        // when, then
        assertThrows(IllegalArgumentException.class, () -> userService.login(email, new Password(originalPassword)));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않아 로그인에 실패한다.")
    void loginFailDueToDifferentPassword() {
        // gvien
        String originalPassword = "validPassword";
        String differentPassword = "differentPassword";
        Email email = new Email("email@wanted.com");
        Password password = new Password(originalPassword);
        String encryptedPassword = passwordEncoder.encrypt(password.getPassword());
        password.changeEncryptedPassword(encryptedPassword);

        User user = new User(email, password);

        userRepository.save(user);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> userService.login(email, new Password(differentPassword)));
    }
}