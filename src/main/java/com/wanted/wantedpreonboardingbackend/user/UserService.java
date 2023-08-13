package com.wanted.wantedpreonboardingbackend.user;

import com.wanted.wantedpreonboardingbackend.common.PasswordEncoder;
import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import com.wanted.wantedpreonboardingbackend.user.domain.Password;
import com.wanted.wantedpreonboardingbackend.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(Email email, Password password) {
        validateDuplicateEmail(email);

        password.changeEncryptedPassword(getEncryptPassword(password));

        User user = new User(email, password);
        userRepository.save(user);

        return user.getId();
    }

    private void validateDuplicateEmail(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    private String getEncryptPassword(Password password) {
        return passwordEncoder.encrypt(password.getPassword());
    }
}
