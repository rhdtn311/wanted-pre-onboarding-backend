package com.wanted.wantedpreonboardingbackend.jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtPayload {

    private static final String ISSUE = "iss";
    private static final String SUBJECT = "sub";
    private static final String AUDIENCE = "aud";
    private static final String NOT_BEFORE = "nbf";
    private static final String ISSUED_AT = "iat";
    private static final String JWT_ID = "jti";
    private static final String EMAIL = "email";

    private static final String DEFAULT_ISSUE_VALUE = "https://wanted.com/auth";

    private final Map<String, Object> payLoads = new HashMap<>();

    public Map<String, Object> getPayLoads() {
        return payLoads;
    }

    public static JwtPayload createDefault(String emailAddress) {
        JwtPayload payload = new JwtPayload();
        payload.setIssue(DEFAULT_ISSUE_VALUE);
        payload.setSubject(emailAddress);
        payload.setJwtId(UUID.randomUUID().toString());
        payload.setUserEmail(emailAddress);

        return payload;
    }

    public void setIssue(String value) {
        payLoads.put(ISSUE, value);
    }

    public void setSubject(String value) {
        payLoads.put(SUBJECT, value);
    }

    public void setAudience(String value) {
        payLoads.put(AUDIENCE, value);
    }

    public void setNotBefore(String value) {
        payLoads.put(NOT_BEFORE, value);
    }

    public void setIssuedAt(String value) {
        payLoads.put(ISSUED_AT, value);
    }

    public void setJwtId(String value) {
        payLoads.put(JWT_ID, value);
    }

    public void setUserEmail(String emailAddress) {
        payLoads.put(EMAIL, emailAddress);
    }

    public void setCustomPayload(String key, String value) {
        payLoads.put(key, value);
    }
}
