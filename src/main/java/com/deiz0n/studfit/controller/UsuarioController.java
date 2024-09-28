package com.deiz0n.studfit.controller;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.response.ResponseRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UsuarioController {

    @PostMapping
    public ResponseEntity<ResponseRequest> create(@RequestBody @Valid UsuarioDTO request);

}
