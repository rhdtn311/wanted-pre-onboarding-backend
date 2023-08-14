package com.wanted.wantedpreonboardingbackend.user.domain;

import com.wanted.wantedpreonboardingbackend.user.domain.validation.PasswordValid;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PasswordValid
@Embeddable
public class Password {

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    public void changeEncryptedPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}
