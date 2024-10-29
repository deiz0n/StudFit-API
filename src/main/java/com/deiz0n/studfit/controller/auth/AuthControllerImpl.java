package com.deiz0n.studfit.controller.auth;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
import com.deiz0n.studfit.domain.dtos.RecoveryPasswordDTO;
import com.deiz0n.studfit.domain.dtos.ResetPasswordDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
@RequestMapping("api/v1.0/auth")
public class AuthControllerImpl implements AuthController {

    private AuthService service;

    public AuthControllerImpl(AuthService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response> singIn(AuthDTO request, ServletWebRequest path) {
        var token = service.signIn(request);
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(token)
                        .build());
    }

    @Override
    public ResponseEntity<Response> recoveryPassword(RecoveryPasswordDTO request, ServletWebRequest path) {
        service.recovery(request);
        return ResponseEntity.accepted()
                .body(Response.builder()
                        .code(HttpStatus.ACCEPTED.value())
                        .status(HttpStatus.ACCEPTED)
                        .path(path.getRequest().getRequestURI())
                        .data("Email enviado")
                        .build());
    }

    @Override
    public ResponseEntity<Response> setPassword(String codigo, ResetPasswordDTO request, ServletWebRequest path) {
        service.reset(codigo, request);
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data("Senha alterada com sucesso")
                        .build());
    }


}
