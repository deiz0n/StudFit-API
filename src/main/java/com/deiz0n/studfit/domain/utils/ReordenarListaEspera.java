package com.deiz0n.studfit.domain.utils;

import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotFoundException;
import com.deiz0n.studfit.infrastructure.repositories.AlunoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.UUID;

@Component
public class ReordenarListaEspera {

    private final AlunoRepository alunoRepository;

    public ReordenarListaEspera(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void reordenar(UUID alunoId) {
        Aluno alunoEfetivado = alunoRepository
                .findById(alunoId)
                .orElseThrow(
                        () -> new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", alunoId))
                );
        var turno = alunoEfetivado.getHorario().getTurno().getNome();
        var listaAlunos = alunoRepository.buscarAlunosPorTurno(turno);

        if (listaAlunos.isEmpty()) return;

        listaAlunos.sort(Comparator.comparingInt(Aluno::getColocacao));

        int colocacao = 1;
        for (Aluno aluno : listaAlunos) {
            aluno.setColocacao(colocacao++);
        }
        alunoRepository.saveAll(listaAlunos);
    }

}
