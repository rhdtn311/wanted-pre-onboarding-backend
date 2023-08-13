package com.wanted.wantedpreonboardingbackend.jwt;

import com.wanted.wantedpreonboardingbackend.user.domain.Email;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenManager {

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

    public Email getUserEmailFromToken(String token) {
        try {
            Claims claims = extractBody(token);
            String emailAddress = (String) claims.get("email");
            return new Email(emailAddress);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        } catch (Exception e) {
            throw new IllegalArgumentException("사용자 인증에 실패했습니다.");
        }
    }

    private Claims extractBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpiredDate() {
        Date now = new Date();
        now.setTime(now.getTime() + EXPIRED_TIME);

        return now;
    }
}
