package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
import com.deiz0n.studfit.domain.dtos.TokenDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.events.TokenGeneratedEvent;
import com.deiz0n.studfit.domain.events.TokenGenerationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private TokenDTO token;
    private AuthenticationManager manager;
    private ApplicationEventPublisher eventPublisher;

    public AuthService(AuthenticationManager manager, ApplicationEventPublisher eventPublisher) {
        this.manager = manager;
        this.eventPublisher = eventPublisher;
    }

    public TokenDTO signIn(AuthDTO auth) {
        var user = new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getSenha());
        var authentication = manager.authenticate(user);
        var tokenGeneration = TokenGenerationEvent.builder()
                .usuario((UsuarioDTO) authentication.getPrincipal())
                .build();

        eventPublisher.publishEvent(tokenGeneration);

        return token;
    }

    @EventListener
    private void getToken(TokenGeneratedEvent tokenGenerated) {
        token = TokenDTO.builder()
                .token(tokenGenerated.getToken())
                .build();
    }
}
