package com.deiz0n.studfit.domain.utils;

import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.domain.enums.Status;
import com.deiz0n.studfit.domain.events.AtualizarVagasDisponiveisHorarioEvent;
import com.deiz0n.studfit.infrastructure.repositories.AlunoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ProcessarAlocacaoAluno {

    private final ApplicationEventPublisher eventPublisher;
    private final AlunoRepository alunoRepository;
    private final ReordenarListaEspera reordenarListaEspera;

    public ProcessarAlocacaoAluno(
            ApplicationEventPublisher eventPublisher,
            AlunoRepository alunoRepository,
            ReordenarListaEspera reordenarListaEspera
    ) {
        this.eventPublisher = eventPublisher;
        this.alunoRepository = alunoRepository;
        this.reordenarListaEspera = reordenarListaEspera;
    }

    @Transactional
    public void processar(Optional<Horario> horarioEscolhido, Aluno aluno) {
        horarioEscolhido.ifPresent(horario -> {
            eventPublisher.publishEvent(
                    new AtualizarVagasDisponiveisHorarioEvent(horario.getId(), false)
            );

            aluno.setHorario(horario);
            aluno.setListaEspera(false);
            aluno.setColocacao(null);
            aluno.setStatus(Status.CADASTRO_PENDENTE);
            alunoRepository.save(aluno);

            reordenarListaEspera.reordenar(aluno.getId());
        });
    }

}
