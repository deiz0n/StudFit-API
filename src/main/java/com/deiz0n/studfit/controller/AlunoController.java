package com.deiz0n.studfit.controller;

import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.response.ResponseRequest;
import com.deiz0n.studfit.services.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1.0/alunos")
public class AlunoController {

    private AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping("lista-espera")
    public ResponseEntity<ResponseRequest> getAlunosListaEspera(ServletWebRequest path) {
        var alunos = service.getListaDeEspera();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(ResponseRequest.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(alunos)
                        .build());
    }

    @GetMapping("efetivados")
    public ResponseEntity<ResponseRequest> getAlunosEfetivados(ServletWebRequest path) {
        var alunos = service.getEfetivados();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(ResponseRequest.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(alunos)
                        .build());
    }

    @PostMapping("/lista-espera/register")
    public ResponseEntity<ResponseRequest> registerAlunoListaEspera(@RequestBody @Valid AlunoListaEsperaDTO request, ServletWebRequest path) {
        var aluno = service.registerListaEspera(request);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(ResponseRequest.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(aluno)
                        .build());
    }

    @DeleteMapping("/lista-espera/delete/{id}")
    public ResponseEntity<ResponseRequest> removeAlunoListaEspera(@PathVariable UUID id, ServletWebRequest path) {
        var aluno = service.removeListaEspera(id);
        return ResponseEntity.noContent().build();
    }
}
