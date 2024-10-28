package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.entites.Aluno;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class HorarioRegisterVagasDisponiveisEvent extends ApplicationEvent {

    private UUID id;

    public HorarioRegisterVagasDisponiveisEvent(Object source, UUID id) {
        super(source);
        this.id = id;
    }
}
