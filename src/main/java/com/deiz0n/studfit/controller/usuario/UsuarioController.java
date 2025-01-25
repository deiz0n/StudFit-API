package com.deiz0n.studfit.controller.usuario;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Controller do Usuário")
public interface UsuarioController {

    @GetMapping
    ResponseEntity<Response> buscarUsuarios(ServletWebRequest path);

    @Transactional
    @PostMapping("/registrar")
    ResponseEntity<Response> registrar(@RequestBody @Valid UsuarioDTO request, ServletWebRequest path);

    @Transactional
    @DeleteMapping("/excluir/{id}")
    ResponseEntity excluir(@PathVariable UUID id);

}
