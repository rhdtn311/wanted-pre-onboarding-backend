package com.wanted.wantedpreonboardingbackend.jwt;

import io.jsonwebtoken.Header;

import java.util.HashMap;
import java.util.Map;

public class JwtHeader {

    private static final String TYPE = "typ";
    private static final String ALGORITHM = "alg";
    private static final String DEFAULT_TYPE_VALUE = "jwt";
    private static final String DEFAULT_ALGORITHM_VALUE = "hs256";

    private final Map<String, Object> headers = new HashMap<>();

    public static JwtHeader createDefault() {
        JwtHeader jwtHeader = new JwtHeader();
        jwtHeader.setType(DEFAULT_TYPE_VALUE);
        jwtHeader.setAlgorithm(DEFAULT_ALGORITHM_VALUE);

        return new JwtHeader();
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setType(String value) {
        headers.put(TYPE, value);
    }

    public void setAlgorithm(String value) {
        headers.put(ALGORITHM, value);
    }
}
