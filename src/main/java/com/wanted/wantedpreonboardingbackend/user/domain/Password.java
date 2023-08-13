package com.wanted.wantedpreonboardingbackend.user.domain;

import com.wanted.wantedpreonboardingbackend.user.domain.validation.PasswordValid;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@PasswordValid
@Embeddable
public class Password {

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;
}
