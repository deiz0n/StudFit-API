package com.deiz0n.studfit.controller;

import com.deiz0n.studfit.domain.response.ResponseRequest;
import com.deiz0n.studfit.services.AlunoService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1.0/alunos")
public class AlunoController {

    private AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping("lista-espera")
    public ResponseEntity<ResponseRequest> getListaDeEspera(ServletWebRequest request) {
        var alunos = service.getListaDeEspera();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(ResponseRequest.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(request.getRequest().getRequestURI())
                        .data(alunos)
                        .build());
    }
}
