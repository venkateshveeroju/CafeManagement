package com.restaurant.tablebookingapp.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {

    private final String secret = System.getenv("JWT_SECRET");

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUserName(String token) {
        return (String) extractClaims(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return (String) extractClaims(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return (String) extractClaims(token, Claims::getSubject);
    }

    public String extractContactNumber(String token) {
        return (String) extractClaims(token, Claims::getSubject);
    }
    public String extractUserId(String token) {
        return (String) extractClaims(token, Claims::getId);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    public <T> Object extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject).toString();
    }


    public Date extractExpiration(String token) {
        return (Date) extractClaims(token, Claims::getExpiration);
    }

    Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


}
