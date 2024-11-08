package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.*;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.events.*;
import com.deiz0n.studfit.domain.exceptions.usuario.UsuarioNotFoundException;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
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
        var tokenGeneration = new TokenGenerationEvent(this, (Usuario) authentication.getPrincipal());

        eventPublisher.publishEvent(tokenGeneration);

        return token;
    }

    public void recovery(RecoveryPasswordDTO recoveryPassword) {
        var recoveryPasswordEvent = new UsuarioRecoveryPassswordEvent(this, recoveryPassword.getEmail());
        eventPublisher.publishEvent(recoveryPasswordEvent);
    }

    public void reset(String codigo, ResetPasswordDTO resetPasswordDTO) {
        var resetPasswordEvent = new UsuarioResetPasswordEvent(this, codigo, resetPasswordDTO);
        eventPublisher.publishEvent(resetPasswordEvent);
    }

    public void validateToken(TokenDTO tokenDTO) {
        var validateTokenEvent = new AuthValidateTokenEvent(this, tokenDTO.getToken());
        eventPublisher.publishEvent(validateTokenEvent);
    }

    @EventListener
    private void getToken(TokenGeneratedEvent tokenGenerated) {
        token = TokenDTO.builder()
                .token(tokenGenerated.getToken())
                .build();
    }
}
