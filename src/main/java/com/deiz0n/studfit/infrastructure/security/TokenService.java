package com.deiz0n.studfit.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String ISSUER = "stud-fit";
    @Value("${api.secret.key.jwt}")
    private String secret;

    public void generateToken(String email) {
        try {
            var algorithm = Algorithm.HMAC256(secret);

            var token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(email)
                    .withExpiresAt(expirationInstant())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao criar Token", e);
        }
    }

    public String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    public Instant expirationInstant() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("03-00"));
    }

}
