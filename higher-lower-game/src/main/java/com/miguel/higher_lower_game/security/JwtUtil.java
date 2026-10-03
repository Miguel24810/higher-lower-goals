package com.miguel.higher_lower_game.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey clave;
    private final long expiracionMs = 1000 * 60 * 60; // 1 hora

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes");
        }
        this.clave = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generarToken(String nombreUsuario) {
        return Jwts.builder()
                .subject(nombreUsuario)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracionMs))
                .signWith(clave)
                .compact();
    }

    public String extraerNombreUsuario(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}