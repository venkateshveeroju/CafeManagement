package com.restaurant.tablebookingapp.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class JwtIssuer {
    private static final Logger logger = LoggerFactory.getLogger(JwtIssuer.class);

    private String secret = "thisismysecretkeydude";


    public String issue(long userId, String email, List<String> roles) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .withClaim("email", email)
                .withClaim("auth", roles)
                //  .withClaim("p", "READ_PRIVILEGE")
                .sign(Algorithm.HMAC256(secret));
    }

    public Long getUserIdFromToken(String token) {
        try{
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong((String) claims.get("email"));

    } catch (ExpiredJwtException e) {
        throw new IllegalStateException("JWT token has expired.", e); // Token expired
    } catch (MalformedJwtException e) {
        throw new IllegalStateException("JWT token is malformed.", e); // Invalid token format
    } catch (Exception e) {
        throw new RuntimeException("Failed to parse JWT.", e); // General exception
    }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
