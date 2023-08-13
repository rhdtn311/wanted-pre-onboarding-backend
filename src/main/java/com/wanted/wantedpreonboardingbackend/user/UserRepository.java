package com.wanted.wantedpreonboardingbackend.user;

import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import com.wanted.wantedpreonboardingbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);
}
