package com.wanted.wantedpreonboardingbackend.user.domain.validation;

import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements ConstraintValidator<EmailValid, Email> {

    private static final String EMAIL_AT_SIGN = "@";

    @Override
    public boolean isValid(Email email, ConstraintValidatorContext context) {
        return isContainEmailAtSign(email.getAddress());
    }

    private boolean isContainEmailAtSign(String address) {
        return address.contains(EMAIL_AT_SIGN);
    }
}
