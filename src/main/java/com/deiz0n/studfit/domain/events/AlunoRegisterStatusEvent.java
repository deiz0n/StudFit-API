package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class AlunoRegisterStatusEvent extends ApplicationEvent {

    private UUID id;

    public AlunoRegisterStatusEvent(Object source, UUID id) {
        super(source);
        this.id = id;
    }
}
