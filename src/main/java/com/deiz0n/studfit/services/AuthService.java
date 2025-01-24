package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.*;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.events.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private TokenDTO token;
    private final AuthenticationManager manager;
    private final ApplicationEventPublisher eventPublisher;

    public AuthService(AuthenticationManager manager, ApplicationEventPublisher eventPublisher) {
        this.manager = manager;
        this.eventPublisher = eventPublisher;
    }

    public TokenDTO login(AuthDTO auth) {
        var usuario = new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getSenha());
        var usuarioAutenticado = manager.authenticate(usuario);
        var gerarToken = new TokenGenerationEvent(this, (Usuario) usuarioAutenticado.getPrincipal());

        eventPublisher.publishEvent(gerarToken);

        return token;
    }

    public void recuperarSenha(RecoveryPasswordDTO recoveryPassword) {
        var recoveryPasswordEvent = new UsuarioRecoveryPassswordEvent(this, recoveryPassword.getEmail());
        eventPublisher.publishEvent(recoveryPasswordEvent);
    }

    public void atualizaSenha(String codigo, ResetPasswordDTO resetPasswordDTO) {
        var resetPasswordEvent = new UsuarioResetPasswordEvent(this, codigo, resetPasswordDTO);
        eventPublisher.publishEvent(resetPasswordEvent);
    }

    public void validarToken(TokenDTO tokenDTO) {
        var validateTokenEvent = new AuthValidateTokenEvent(this, tokenDTO.getToken());
        eventPublisher.publishEvent(validateTokenEvent);
    }

    @EventListener
    private void obterTokenGerado(TokenGeneratedEvent tokenGenerated) {
        token = TokenDTO.builder()
                .token(tokenGenerated.getToken())
                .build();
    }
}
