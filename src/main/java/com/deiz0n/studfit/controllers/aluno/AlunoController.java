package com.deiz0n.studfit.controllers.aluno;

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
    ResponseEntity<Response<?>> buscarAluno(@PathVariable UUID id, ServletWebRequest path);

    @GetMapping("lista-espera")
    ResponseEntity<Response<?>> buscarAlunosListaEspera(ServletWebRequest path, @RequestParam(defaultValue = "0") int numeroPagina, @RequestParam(defaultValue = "10") int quantidade, @RequestParam String turno);

    @Transactional
    @PostMapping("/lista-espera/registrar")
    ResponseEntity<Response<?>> registrarAlunoListaEspera(@RequestBody @Valid AlunoListaEsperaDTO request, ServletWebRequest path);

    @Transactional
    @DeleteMapping("/lista-espera/excluir/{id}")
    ResponseEntity excluirAlunoListaEspera(@PathVariable UUID id, ServletWebRequest path);

    @GetMapping("efetivados")
    ResponseEntity<Response<?>> buscarAlunosEfetivados(ServletWebRequest path, @RequestParam(defaultValue = "0") int numeroPagina, @RequestParam(defaultValue = "100") int quantidade);

//    @Deprecated
//    @Transactional
//    @PatchMapping("efetivados/efetivar")
//    ResponseEntity<Response<?>> registrarAlunoEfetivado(@RequestBody @Valid AlunoDTO request, ServletWebRequest path);

    @Transactional
    @DeleteMapping("efetivados/excluir/{id}")
    ResponseEntity<?> excluirAlunoEfetivado(@PathVariable UUID id);

    @Transactional
    @PatchMapping("efetivado/atualizar/{id}")
    ResponseEntity<Response<?>> atualizarAlunoEfetivado(@PathVariable UUID id, @RequestBody @Valid AlunoDTO request, ServletWebRequest path);
}
