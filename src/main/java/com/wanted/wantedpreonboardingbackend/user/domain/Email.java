package com.wanted.wantedpreonboardingbackend.user.domain;

import com.wanted.wantedpreonboardingbackend.user.domain.validation.EmailValid;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@EmailValid
@Embeddable
public class Email {

    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String address;
}
