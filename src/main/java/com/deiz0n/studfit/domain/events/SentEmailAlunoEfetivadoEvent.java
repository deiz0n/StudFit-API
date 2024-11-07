package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SentEmailAlunoEfetivadoEvent extends ApplicationEvent {

    private final String[] destinatario;
    private final String nomeAluno;

    public SentEmailAlunoEfetivadoEvent(Object source, String[] destinatario, String nomeAluno) {
        super(source);
        this.destinatario = destinatario;
        this.nomeAluno = nomeAluno;
    }
}
