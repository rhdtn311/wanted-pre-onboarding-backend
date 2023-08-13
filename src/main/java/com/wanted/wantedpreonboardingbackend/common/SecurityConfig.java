package com.wanted.wantedpreonboardingbackend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BcryptPasswordEncoder();
    }
}
