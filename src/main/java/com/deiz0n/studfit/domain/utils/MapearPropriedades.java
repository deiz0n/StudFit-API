package com.deiz0n.studfit.domain.utils;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.domain.events.AtualizarVagasDisponiveisHorarioEvent;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MapearPropriedades {

    private final ModelMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public MapearPropriedades(ModelMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    public void mapearPropriedadesAluno(Aluno aluno, AlunoDTO alunoDTO) {
        if (alunoDTO.getHorario() != null) {
            var horarioNovo = mapper.map(alunoDTO.getHorario(), Horario.class);
            if (!horarioNovo.equals(aluno.getHorario())) {
                var horarioAntigo = aluno.getHorario();
                eventPublisher.publishEvent(
                        new AtualizarVagasDisponiveisHorarioEvent(horarioAntigo.getId(), true)
                );
                eventPublisher.publishEvent(
                        new AtualizarVagasDisponiveisHorarioEvent(horarioNovo.getId(), false)
                );
                aluno.setHorario(horarioNovo);
            }
        }

        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPropertyCondition(context -> context.getSource() != null);
        mapper.map(alunoDTO, aluno);
    }

}
