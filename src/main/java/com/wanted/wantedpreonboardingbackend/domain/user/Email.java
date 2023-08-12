package com.wanted.wantedpreonboardingbackend.domain.user;

import com.wanted.wantedpreonboardingbackend.domain.user.validation.EmailValid;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@EmailValid
@Embeddable
public class Email {

    @Column(name = "email", nullable = false, unique = true)
    private String address;
}
