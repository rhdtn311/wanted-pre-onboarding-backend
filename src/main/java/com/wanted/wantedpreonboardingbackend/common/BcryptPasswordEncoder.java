package com.wanted.wantedpreonboardingbackend.common;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String inputPassword, String savedPassword) {
        return BCrypt.checkpw(inputPassword, savedPassword);
    }
}
