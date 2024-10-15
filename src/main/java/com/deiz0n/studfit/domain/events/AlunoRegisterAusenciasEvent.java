package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class AlunoRegisterAusenciasEvent extends ApplicationEvent {

    private PresencaDTO presenca;

    public AlunoRegisterAusenciasEvent(Object source, PresencaDTO presenca) {
        super(source);
        this.presenca = presenca;
    }
}
