package com.deiz0n.studfit.controller.auth;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
import com.deiz0n.studfit.domain.dtos.RecoveryPasswordDTO;
import com.deiz0n.studfit.domain.dtos.ResetPasswordDTO;
import com.deiz0n.studfit.domain.dtos.TokenDTO;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;

@Tag(name = "Controller de Autenticação")
public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<Response> singIn(@RequestBody @Valid AuthDTO request, ServletWebRequest path);

    @PostMapping("/recuperar-senha")
    ResponseEntity<Response> recoveryPassword(@RequestBody @Valid RecoveryPasswordDTO request, ServletWebRequest path);

    @PostMapping("/alterar-senha/")
    ResponseEntity<Response> setPassword(@RequestParam String codigo, @RequestBody @Valid ResetPasswordDTO request, ServletWebRequest path);

    @PostMapping("/validar-token")
    ResponseEntity<Response> validateToken(@RequestBody TokenDTO request, ServletWebRequest path);

}
