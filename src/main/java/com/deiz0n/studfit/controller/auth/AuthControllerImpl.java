package com.deiz0n.studfit.controller.auth;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
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
}
