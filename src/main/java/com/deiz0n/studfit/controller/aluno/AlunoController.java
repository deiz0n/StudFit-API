package com.deiz0n.studfit.controller.aluno;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.response.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

public interface AlunoController {

    @GetMapping("/{id}")
    ResponseEntity<Response> getAluno(@PathVariable UUID id, ServletWebRequest path);
    @GetMapping("lista-espera")
    ResponseEntity<Response> getAlunosListaEspera(ServletWebRequest path);

    @PostMapping("/lista-espera/register")
    ResponseEntity<Response> registerAlunoListaEspera(@RequestBody @Valid AlunoListaEsperaDTO request, ServletWebRequest path);

    @DeleteMapping("/lista-espera/delete/{id}")
    ResponseEntity removeAlunoListaEspera(@PathVariable UUID id, ServletWebRequest path);

    @GetMapping("efetivados")
    ResponseEntity<Response> getAlunosEfetivados(ServletWebRequest path);

    @PatchMapping("efetivados/efetivar")
    ResponseEntity<Response> registerAlunoEfetivado(@RequestBody @Valid AlunoDTO request, ServletWebRequest path);

    @DeleteMapping("efetivados/delete/{id}")
    ResponseEntity removeAlunoEfetivado(@PathVariable UUID id);

    @PatchMapping("efetivado/update/{id}")
    ResponseEntity<Response> updateAlunoEfetivado(@PathVariable UUID id, @RequestBody @Valid AlunoDTO request, ServletWebRequest path);
}
