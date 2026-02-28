package com.backend.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey signingKey;

    @PostConstruct
    void initialize() {
        byte[] decoded = Decoders.BASE64.decode(secretKey);
        signingKey = Keys.hmacShaKeyFor(decoded);
    }

    public String generateAccessToken(Authentication authentication,
                                      Map<String, Object> extraClaims) {

        Map<String, Object> claims = new HashMap<>();

        if (extraClaims != null && !extraClaims.isEmpty()) {
            claims.putAll(extraClaims);
        }

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("authorities", authorities);

        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(authentication.getName())
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessTokenExpiration)))
                .signWith(signingKey)
                .compact();
    }

    public String generateAccessToken(Authentication authentication) {
        return generateAccessToken(authentication, Map.of());
    }

    public String generateRefreshToken(Authentication authentication) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(authentication.getName())
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(refreshTokenExpiration)))
                .signWith(signingKey)
                .compact();
    }

    public @Nullable Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception ex) {
            log.warn("JWT parsing failed: {}", ex.getMessage());
            return null;
        }
    }

    public @Nullable String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        if (claims == null) return List.of();

        Object authorities = claims.get("authorities");

        if (authorities instanceof List<?> list) {
            return list.stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }

    public boolean isTokenValid(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null &&
                claims.getExpiration() != null &&
                claims.getExpiration().after(new Date());
    }
}