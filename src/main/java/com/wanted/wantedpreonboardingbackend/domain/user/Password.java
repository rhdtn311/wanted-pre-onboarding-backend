package com.wanted.wantedpreonboardingbackend.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;
}
