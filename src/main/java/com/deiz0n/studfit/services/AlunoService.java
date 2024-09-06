package com.deiz0n.studfit.services;

import com.deiz0n.studfit.repositories.AlunoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
}
