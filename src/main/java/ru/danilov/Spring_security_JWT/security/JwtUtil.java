package ru.danilov.Spring_security_JWT.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("{jwt_secret}")
    private String secret;

    public String generateToken(UserDetails userDetails) {
        // Логика генерации JWT
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", userDetails.getUsername())
                .withIssuedAt(new Date())
                .withIssuer("danilov")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }


    public String validateToken(String token) {
        // Логика проверки JWT
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("dobrov")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
