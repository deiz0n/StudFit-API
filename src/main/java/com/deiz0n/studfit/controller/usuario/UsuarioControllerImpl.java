package com.deiz0n.studfit.controller.usuario;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1.0/usuarios")
public class UsuarioControllerImpl implements UsuarioController {

    private UsuarioService service;

    public UsuarioControllerImpl(UsuarioService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response> getUsuarios(ServletWebRequest path) {
        var usuarios = service.getAll();
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(usuarios)
                        .build());
    }

    @Override
    public ResponseEntity<Response> create(UsuarioDTO request, ServletWebRequest path) {
        var usuario = service.create(request);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(usuario.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(Response.builder()
                        .code(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .path(path.getRequest().getRequestURI())
                        .data(usuario)
                        .build());
    }
}
