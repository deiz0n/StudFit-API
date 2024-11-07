package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SentAlunoEfetivadoToUsuarios extends ApplicationEvent {

    private final String[] destinatario;
    private final String nomeAluno;

    public SentAlunoEfetivadoToUsuarios(Object source, String[] destinatario, String nomeAluno) {
        super(source);
        this.destinatario = destinatario;
        this.nomeAluno = nomeAluno;
    }
}
