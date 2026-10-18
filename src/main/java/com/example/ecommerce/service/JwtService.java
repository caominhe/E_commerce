package com.example.ecommerce.service;

import com.example.ecommerce.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(JwtProperties properties) {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getSecret());

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = properties.getExpirationMs();
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(secretKey)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
