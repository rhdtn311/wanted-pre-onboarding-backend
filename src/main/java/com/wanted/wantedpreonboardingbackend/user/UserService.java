package com.wanted.wantedpreonboardingbackend.user;

import com.wanted.wantedpreonboardingbackend.common.PasswordEncoder;
import com.wanted.wantedpreonboardingbackend.jwt.JwtTokenManager;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import com.wanted.wantedpreonboardingbackend.user.domain.Password;
import com.wanted.wantedpreonboardingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    @Transactional
    public Long join(Email email, Password password) {
        validateDuplicateEmail(email);

        password.changeEncryptedPassword(getEncryptPassword(password));

        User user = new User(email, password);
        userRepository.save(user);

        return user.getId();
    }

    @Transactional(readOnly = true)
    public String login(Email email, Password password) {
        User user = getUserOrException(email);
        validateCorrectPassword(password.getPassword(), user.getPassword().getPassword());

        return jwtTokenManager.createToken(email.getAddress());
    }

    private void validateDuplicateEmail(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    private String getEncryptPassword(Password password) {
        return passwordEncoder.encrypt(password.getPassword());
    }

    private User getUserOrException(Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private void validateCorrectPassword(String inputPassword, String savedPassword) {
        if (!passwordEncoder.isMatch(inputPassword, savedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
