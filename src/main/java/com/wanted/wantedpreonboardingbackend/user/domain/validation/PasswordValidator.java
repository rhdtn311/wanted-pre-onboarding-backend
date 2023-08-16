package com.wanted.wantedpreonboardingbackend.user.domain.validation;

import com.wanted.wantedpreonboardingbackend.user.domain.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<PasswordValid, Password> {

    private static final int MINIMUM_LENGTH = 8;

    @Override
    public boolean isValid(Password password, ConstraintValidatorContext context) {
        return isLongMinimumLength(password.getPassword());
    }

    private boolean isLongMinimumLength(String password) {
        return password.length() > MINIMUM_LENGTH;
    }
}
