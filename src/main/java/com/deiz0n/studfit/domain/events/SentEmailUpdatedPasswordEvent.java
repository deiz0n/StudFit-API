package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SentEmailUpdatedPasswordEvent extends ApplicationEvent {

    private final String[] destinatario;

    public SentEmailUpdatedPasswordEvent(Object source, String[] destinatario) {
        super(source);
        this.destinatario = destinatario;
    }
}
