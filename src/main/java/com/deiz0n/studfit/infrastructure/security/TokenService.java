package com.deiz0n.studfit.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.deiz0n.studfit.domain.events.ValidarTokenAuthEvent;
import com.deiz0n.studfit.domain.events.GerarTokenAuthEvent;
import com.deiz0n.studfit.domain.events.SolicitarGeracaoTokenAuthEvent;
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
    public void generateToken(SolicitarGeracaoTokenAuthEvent tokenGeneration) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(tokenGeneration.usuario().getEmail())
                    .withExpiresAt(expirationInstant())
                    .sign(algorithm);
            var tokenGerado = new GerarTokenAuthEvent(token);

            eventPublisher.publishEvent(tokenGerado);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao criar Token", e);
        }
    }

    public String validateToken(String token) {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
    }

    @EventListener
    private void validarToken(ValidarTokenAuthEvent tokenEvent) {
        validateToken(tokenEvent.token());
    }

    public Instant expirationInstant() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }

}
