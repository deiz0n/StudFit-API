package com.deiz0n.studfit.controller.presencao;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;

public interface PresencaController {

    @GetMapping
    ResponseEntity<Response> getPresencas(ServletWebRequest path);
    @PostMapping("/create")
    ResponseEntity<Response> createPresenca(@RequestBody PresencaDTO request, ServletWebRequest path);

}
