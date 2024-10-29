package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailRecoveryPasswordEvent extends ApplicationEvent {

    private String destinatario;
    private String codigo;

    public EmailRecoveryPasswordEvent(Object source, String destinatario, String codigo) {
        super(source);
        this.destinatario = destinatario;
        this.codigo = codigo;
    }
}
