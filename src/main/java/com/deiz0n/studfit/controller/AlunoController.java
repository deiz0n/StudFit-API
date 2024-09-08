package com.deiz0n.studfit.controller;

import com.deiz0n.studfit.services.AlunoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1.0/alunos")
public class AlunoController {

    private AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }
}
