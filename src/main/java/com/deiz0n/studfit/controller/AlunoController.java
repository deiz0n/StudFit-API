package com.deiz0n.studfit.controller;

import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.services.AlunoService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1.0/alunos")
public class AlunoController {

    private AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping("lista-espera")
    public ResponseEntity<List<AlunoListaEsperaDTO>> getListaDeEspera() {
        var alunos = service.getListaDeEspera();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .body(alunos);
    }
}
