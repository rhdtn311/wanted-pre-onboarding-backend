package com.wanted.wantedpreonboardingbackend.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String KEY = "JWT_TOKEN_SECRET_KEY_JWT_TOKEN_SECRET_KEY";
    private static final Long EXPIRED_TIME = 300000L;

    public String createToken(String emailAddress) {
        Date expiredDate = getExpiredDate();

        return Jwts.builder()
                .setHeader(JwtHeader.createDefault().getHeaders())
                .setClaims(JwtPayload.createDefault(emailAddress).getPayLoads())
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes()))
                .compact();
    }

    private Date getExpiredDate() {
        Date now = new Date();
        now.setTime(now.getTime() + EXPIRED_TIME);

        return now;
    }
}
