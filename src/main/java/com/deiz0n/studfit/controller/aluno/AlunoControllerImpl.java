package com.deiz0n.studfit.controller.aluno;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.response.Response;
import com.deiz0n.studfit.services.AlunoService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1.0/alunos")
public class AlunoControllerImpl implements AlunoController {

    private AlunoService service;

    public AlunoControllerImpl(AlunoService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Response> getAluno(UUID id, ServletWebRequest path) {
        var aluno = service.getAlunoById(id);
        return ResponseEntity.ok()
                .body(Response.builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .path(path.getRequest().getRequestURI())
                        .data(aluno)
                        .build());
    }

    @Override
    public ResponseEntity<Response> getAlunosListaEspera(ServletWebRequest path) {
        var alunos = service.getListaDeEspera();
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
    public ResponseEntity<Response> registerAlunoListaEspera(AlunoListaEsperaDTO request, ServletWebRequest path) {
        var aluno = service.registerListaEspera(request);
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
    public ResponseEntity removeAlunoListaEspera(UUID id, ServletWebRequest path) {
        var aluno = service.removeListaEspera(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Response> getAlunosEfetivados(ServletWebRequest path) {
        var alunos = service.getEfetivados();
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
    public ResponseEntity<Response> registerAlunoEfetivado(AlunoDTO request, ServletWebRequest path) {
        var aluno = service.registerEfetivado(request);
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
    public ResponseEntity removeAlunoEfetivado(UUID id) {
        service.removeEfetivado(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Response> updateAlunoEfetivado(UUID id, AlunoDTO request, ServletWebRequest path) {
        var aluno = service.updateEfetivado(id, request);
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
