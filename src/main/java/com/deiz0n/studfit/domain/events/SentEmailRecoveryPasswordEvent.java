package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SentEmailRecoveryPasswordEvent extends ApplicationEvent {

    private final String[] destinatario;
    private final String codigo;

    public SentEmailRecoveryPasswordEvent(Object source, String[] destinatario, String codigo) {
        super(source);
        this.destinatario = destinatario;
        this.codigo = codigo;
    }
}
