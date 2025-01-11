package com.deiz0n.studfit.controller.aluno;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;

@Tag(name = "Controller do Aluno")
public interface AlunoController {

    @GetMapping("/{id}")
    ResponseEntity<Response> getAluno(@PathVariable UUID id, ServletWebRequest path);

    @GetMapping("lista-espera")
    ResponseEntity<Response> getAlunosListaEspera(ServletWebRequest path);

    @Transactional
    @PostMapping("/lista-espera/registrar")
    ResponseEntity<Response> registerAlunoListaEspera(@RequestBody @Valid AlunoListaEsperaDTO request, ServletWebRequest path);

    @Transactional
    @DeleteMapping("/lista-espera/excluir/{id}")
    ResponseEntity removeAlunoListaEspera(@PathVariable UUID id, ServletWebRequest path);

    @GetMapping("efetivados")
    ResponseEntity<Response> getAlunosEfetivados(ServletWebRequest path);

    @Transactional
    @PatchMapping("efetivados/efetivar")
    ResponseEntity<Response> registerAlunoEfetivado(@RequestBody @Valid AlunoDTO request, ServletWebRequest path);

    @Transactional
    @DeleteMapping("efetivados/excluir/{id}")
    ResponseEntity removeAlunoEfetivado(@PathVariable UUID id);

    @Transactional
    @PatchMapping("efetivado/atualilzar/{id}")
    ResponseEntity<Response> updateAlunoEfetivado(@PathVariable UUID id, @RequestBody @Valid AlunoDTO request, ServletWebRequest path);
}
