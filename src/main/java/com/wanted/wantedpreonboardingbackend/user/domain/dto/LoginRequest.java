package com.wanted.wantedpreonboardingbackend.user.domain.dto;

import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import com.wanted.wantedpreonboardingbackend.user.domain.Password;
import jakarta.validation.Valid;

public record LoginRequest(
        @Valid
        Email email,

        @Valid
        Password password
) {
}
