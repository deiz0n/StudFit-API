package com.deiz0n.studfit.controllers.aluno;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.AlunoService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1.0/alunos")
public class AlunoControllerImpl implements AlunoController {

    private final AlunoService service;

    public AlunoControllerImpl(AlunoService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response<?>> buscarAluno(UUID id, ServletWebRequest path) {
        var aluno = service.buscarAlunoPorId(id);
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(aluno)
                        .build());
    }

    @Override
    public ResponseEntity<Response<?>> buscarAlunosListaEspera(ServletWebRequest path, @RequestParam(defaultValue = "0") int numeroPagina, @RequestParam(defaultValue = "10") int quantidade, @RequestParam String turno) {
        var alunos = service.buscarAlunosListaEspera(numeroPagina, quantidade, turno);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(alunos)
                        .build());
    }

    @Override
    public ResponseEntity<Response<?>> registrarAlunoListaEspera(AlunoListaEsperaDTO request, ServletWebRequest path) {
        var aluno = service.registrarAlunosListaEspera(request);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(aluno)
                        .build());
    }

    @Override
    public ResponseEntity<?> excluirAlunoListaEspera(UUID id, ServletWebRequest path) {
        service.excluirAlunoListaEspera(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Response<?>> buscarAlunosEfetivados(ServletWebRequest path, @RequestParam(defaultValue = "0") int numeroPagina, @RequestParam(defaultValue = "10") int quantidade) {
        var alunos = service.buscarAlunosEfetivados(numeroPagina, quantidade);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(alunos)
                        .build());
    }

//    @Override
//    public ResponseEntity<Response<?>> registrarAlunoEfetivado(AlunoDTO request, ServletWebRequest path) {
//        var aluno = service.registrarAlunoEfetivado(request);
//        return ResponseEntity.ok()
//                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
//                .body(Response.builder()
//                        .code(HttpStatus.OK.value())
//                        .status(HttpStatus.OK)
//                        .path(path.getRequest().getRequestURI())
//                        .data(aluno)
//                        .build());
//    }

    @Override
    public ResponseEntity<?> excluirAlunoEfetivado(UUID id) {
        service.excluirAlunoEfetivado(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Response<?>> atualizarAlunoEfetivado(UUID id, AlunoDTO request, ServletWebRequest path) {
        var aluno = service.atualizarEfetivado(id, request);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(aluno)
                        .build());
    }

}
