package com.deiz0n.studfit.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.deiz0n.studfit.domain.events.TokenGeneratedEvent;
import com.deiz0n.studfit.domain.events.TokenGenerationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String ISSUER = "stud-fit";
    @Value("${api.secret.key.jwt}")
    private String secret;
    private ApplicationEventPublisher eventPublisher;

    public TokenService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void generateToken(TokenGenerationEvent tokenGeneration) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(tokenGeneration.getUsuario().getEmail())
                    .withExpiresAt(expirationInstant())
                    .sign(algorithm);
            var tokenGenerated = new TokenGeneratedEvent(this, token);

            eventPublisher.publishEvent(tokenGenerated);

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
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }

}
