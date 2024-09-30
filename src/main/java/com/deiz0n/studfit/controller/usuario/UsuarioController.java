package com.deiz0n.studfit.controller.usuario;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;

public interface UsuarioController {

    @GetMapping
    public ResponseEntity<Response> getUsuarios(ServletWebRequest path);
    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestBody @Valid UsuarioDTO request, ServletWebRequest path);

}
