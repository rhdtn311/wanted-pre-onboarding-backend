package com.wanted.wantedpreonboardingbackend.common;

public interface PasswordEncoder {
    String encrypt(String password);
    boolean isMatch(String inputPassword, String savedPassword);
}
