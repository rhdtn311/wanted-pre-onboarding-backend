package com.wanted.wantedpreonboardingbackend.domain.user;

import com.wanted.wantedpreonboardingbackend.domain.user.validation.PasswordValid;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@PasswordValid
@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;
}
