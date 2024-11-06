package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SentEmailDeletedAlunoEfetivadoEvent extends ApplicationEvent {

    private final String[] destinatario;
    private final String nome;

    public SentEmailDeletedAlunoEfetivadoEvent(Object source, String[] destinatario, String nome) {
        super(source);
        this.destinatario = destinatario;
        this.nome = nome;
    }
}
