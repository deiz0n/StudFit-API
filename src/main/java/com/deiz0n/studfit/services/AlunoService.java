package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.repositories.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    // Retorna todos os alunos que estão na lista de espera
    public List<AlunoDTO> getListaDeEspera() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> AlunoDTO.builder()
                        .nome(aluno.getNome())
                        .email(aluno.getEmail())
                        .colocacao(aluno.getColocacao())
                        .build()
                )
                .filter(aluno -> !aluno.getAtivado())
                .collect(Collectors.toList());
    }
}
