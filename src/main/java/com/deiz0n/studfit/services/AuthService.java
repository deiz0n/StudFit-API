package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
import com.deiz0n.studfit.domain.dtos.RecoveryPasswordDTO;
import com.deiz0n.studfit.domain.dtos.TokenDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.events.TokenGeneratedEvent;
import com.deiz0n.studfit.domain.events.TokenGenerationEvent;
import com.deiz0n.studfit.domain.events.UsuarioRecoveryPassswordEvent;
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
    private UsuarioRepository usuarioRepository;

    public AuthService(AuthenticationManager manager, ApplicationEventPublisher eventPublisher, UsuarioRepository usuarioRepository) {
        this.manager = manager;
        this.eventPublisher = eventPublisher;
        this.usuarioRepository = usuarioRepository;
    }

    public TokenDTO signIn(AuthDTO auth) {
        var user = new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getSenha());
        var authentication = manager.authenticate(user);
        var tokenGeneration = new TokenGenerationEvent(this, (Usuario) authentication.getPrincipal());

        eventPublisher.publishEvent(tokenGeneration);

        return token;
    }

    public void recovery(RecoveryPasswordDTO recoveryPassword) {
        var usuario = usuarioRepository.findByEmail(recoveryPassword.getEmail());
        if (usuario.isEmpty())
            throw new UsuarioNotFoundException("Usuário não encontrado");

        var recoveryPasswordEvent = new UsuarioRecoveryPassswordEvent(this, recoveryPassword.getEmail());
        eventPublisher.publishEvent(recoveryPasswordEvent);
    }

    @EventListener
    private void getToken(TokenGeneratedEvent tokenGenerated) {
        token = TokenDTO.builder()
                .token(tokenGenerated.getToken())
                .build();
    }
}
