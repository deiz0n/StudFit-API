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
        var solicitarToken = new SolicitarGeracaoTokenAuthEvent((Usuario) usuarioAutenticado.getPrincipal());

        eventPublisher.publishEvent(solicitarToken);

        return token;
    }

    public void recuperarSenha(RecoveryPasswordDTO recoveryPassword) {
        var recoveryPasswordEvent = new SolicitarRecuperacaoSenhaEvent(recoveryPassword.getEmail());
        eventPublisher.publishEvent(recoveryPasswordEvent);
    }

    public void atualizaSenha(String codigo, ResetPasswordDTO resetPasswordDTO) {
        var atualizarSenha = new AtualizarSenhaUsuarioEvent(codigo, resetPasswordDTO);
        eventPublisher.publishEvent(atualizarSenha);
    }

    public void validarToken(TokenDTO tokenDTO) {
        var validarToken = new ValidarTokenAuthEvent(tokenDTO.getToken());
        eventPublisher.publishEvent(validarToken);
    }

    @EventListener
    private void obterTokenGerado(GerarTokenAuthEvent evento) {
        token = TokenDTO.builder()
                .token(evento.token())
                .build();
    }
}
