package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Horario;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HorarioRegisterVagasDisponiveisEvent extends ApplicationEvent {

    private Aluno aluno;

    public HorarioRegisterVagasDisponiveisEvent(Object source, Aluno aluno) {
        super(source);
        this.aluno = aluno;
    }
}
