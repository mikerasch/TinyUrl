package com.michael.tinyurl.security.jwt;

import com.michael.tinyurl.security.models.JwtInformation;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtHelper {
    private static final int ONE_DAY_IN_SECONDS = 86400;
    private final Key key;

    public JwtHelper(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Optional<String> extractUsername(String token) {
        try {
            return Optional.ofNullable(Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public JwtInformation createToken(String username) {
        Date issuedAt = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plusSeconds(ONE_DAY_IN_SECONDS));
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
        return new JwtInformation(token, issuedAt, expiration);
    }
}
