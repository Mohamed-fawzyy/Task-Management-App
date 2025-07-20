package com.trading.task_management.security.config;

import com.trading.task_management.security.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;

    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtService(
            @Value("${jwt.access-token.secret}") String accessSecret,
            @Value("${jwt.refresh-token.secret}") String refreshSecret,
            @Value("${jwt.access-token.expiration}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token.expiration}") long refreshTokenExpirationMs
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes());
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    // === Access Token ===
    public String generateAccessToken(User user) {
        return generateToken(new HashMap<>(), user, accessSecretKey, accessTokenExpirationMs);
    }

    // === Refresh Token ===
    public String generateRefreshToken(User user) {
        return generateToken(new HashMap<>(), user, refreshSecretKey, refreshTokenExpirationMs);
    }

    // === Common Token Generator ===
    private String generateToken(Map<String, Object> extraClaims, User user, SecretKey secretKey, long expirationMs) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // === Token Validation ===
    public boolean isTokenValid(String token, User user, boolean isRefresh) {
        final String email = extractUsername(token, isRefresh);  // Pass isRefresh here
        return email.equals(user.getEmail()) && !isTokenExpired(token, isRefresh);  // Pass isRefresh here
    }

    public boolean isTokenExpired(String token, boolean isRefresh) {
        return extractExpiration(token, isRefresh).before(new Date());
    }

    public String extractUsername(String token, boolean isRefresh) {
        return extractClaim(token, Claims::getSubject, isRefresh);
    }

    public Date extractExpiration(String token, boolean isRefresh) {
        return extractClaim(token, Claims::getExpiration, isRefresh);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefresh) {
        final Claims claims = extractAllClaims(token, isRefresh);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, boolean isRefresh) {
        SecretKey key = isRefresh ? refreshSecretKey : accessSecretKey;
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
