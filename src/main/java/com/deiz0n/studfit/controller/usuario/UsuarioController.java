package com.deiz0n.studfit.controller.usuario;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

public interface UsuarioController {

    @GetMapping
    ResponseEntity<Response> getUsuarios(ServletWebRequest path);
    @PostMapping("/create")
    ResponseEntity<Response> createUsuario(@RequestBody @Valid UsuarioDTO request, ServletWebRequest path);
    @DeleteMapping("/delete/{id}")
    ResponseEntity deleteUsuario(@PathVariable UUID id);

}
