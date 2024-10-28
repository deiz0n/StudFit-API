package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlunoDeletedByAusenciasEvent extends ApplicationEvent {

    private AlunoDTO aluno;

    public AlunoDeletedByAusenciasEvent(Object source, AlunoDTO aluno) {
        super(source);
        this.aluno = aluno;
    }
}
