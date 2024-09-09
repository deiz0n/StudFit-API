package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
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
    public List<AlunoListaEsperaDTO> getListaDeEspera() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> new AlunoListaEsperaDTO(
                            aluno.getId(),
                            aluno.getNome(),
                            aluno.getEmail(),
                            aluno.getColocacao(),
                            aluno.getListaEspera()
                        )
                )
                .filter(AlunoListaEsperaDTO::getListaEspera)
                .collect(Collectors.toList());
    }

    // Retorna todos os alunos já cadastrados na academia
    public List<AlunoDTO> getEfetivados() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> new AlunoDTO(
                        aluno.getId(),
                        aluno.getNome(),
                        aluno.getPeso(),
                        aluno.getAltura(),
                        aluno.getEmail(),
                        aluno.getTelefone(),
                        aluno.getCirurgias(),
                        aluno.getPatologias(),
                        aluno.getMesesExperienciaMusculacao(),
                        aluno.getDiagnosticoLesaoJoelho(),
                        aluno.getStatus(),
                        aluno.getAusenciasConsecutivas(),
                        aluno.getListaEspera()
                ))
                .filter(aluno -> !aluno.getListaEspera())
                .collect(Collectors.toList());
    }
}
