package com.wanted.wantedpreonboardingbackend.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Email {

    @Column(name = "email", nullable = false, unique = true)
    private String address;
}
