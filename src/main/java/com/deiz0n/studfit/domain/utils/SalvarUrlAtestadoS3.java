package com.deiz0n.studfit.domain.utils;

import com.deiz0n.studfit.domain.events.GerarUrlAtestadoS3Event;
import com.deiz0n.studfit.infrastructure.repositories.AlunoRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SalvarUrlAtestadoS3 {

    private final AlunoRepository alunoRepository;

    public SalvarUrlAtestadoS3(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @EventListener
    @Transactional
    public void salvarUrl(GerarUrlAtestadoS3Event s3Event) {
        var aluno = alunoRepository.getReferenceById(s3Event.alunoId());
        aluno.setAtestado(s3Event.url());
        alunoRepository.save(aluno);
    }

}
