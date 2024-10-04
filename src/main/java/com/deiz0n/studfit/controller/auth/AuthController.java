package com.deiz0n.studfit.controller.auth;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
import com.deiz0n.studfit.domain.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;

public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<Response> singIn(@RequestBody @Valid AuthDTO request, ServletWebRequest path);

}
