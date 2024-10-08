package com.deiz0n.studfit.controller.presencao;

import com.deiz0n.studfit.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface PresencaController {

    @GetMapping
    ResponseEntity<Response> getAll();

}
