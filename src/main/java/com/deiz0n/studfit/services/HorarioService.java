package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.domain.enums.Turno;
import com.deiz0n.studfit.domain.events.HorarioRegisterVagasDisponiveisEvent;
import com.deiz0n.studfit.domain.exceptions.*;
import com.deiz0n.studfit.infrastructure.repositories.HorarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    private HorarioRepository repository;
    private ModelMapper mapper;

    public HorarioService(HorarioRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<HorarioDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(horario -> mapper.map(horario, HorarioDTO.class))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<HorarioDTO> getByTurno(String turno) {
        return repository.getByTurno(validateTurno(turno))
                .stream()
                .map(horario -> mapper.map(horario, HorarioDTO.class))
                .collect(Collectors.toList());
    }

    public HorarioDTO create(HorarioDTO horarioDTO) {
        validateHorarios(horarioDTO);

        var horario = mapper.map(horarioDTO, Horario.class);

        horario.setTurno(defineTurno(horarioDTO));
        repository.save(horario);

        return HorarioDTO.builder()
                .id(horario.getId())
                .horarioInicial(horario.getHorarioInicial())
                .horarioFinal(horario.getHorarioFinal())
                .turno(horario.getTurno())
                .vagasDisponiveis(horario.getVagasDisponiveis())
                .build();
    }

    public void delete(UUID id) {
        var horario = findByID(id);
        repository.delete(horario);
    }

//    @EventListener
//    private void setVagasDisponiveis(HorarioRegisterVagasDisponiveisEvent vagasDisponiveisEvent) {
//        var horario = repository.getByAlunos(List.of(vagasDisponiveisEvent.getAluno()));
//        var vagasDisponiveis = horario.getVagasDisponiveis();
//        var quantityAlunos = horario.getAlunos().size();
//
//        horario.setVagasDisponiveis(vagasDisponiveis - quantityAlunos);
//        repository.save(horario);
//    }

    private Horario findByID(UUID id) {
        return repository.findById(id)
                .map(horario -> mapper.map(horario, Horario.class))
                .orElseThrow(
                        () -> new HorarioNotFoundException(String.format("Horário com ID: %s não foi encontrado", id.toString()))
                );
    }

    private void validateHorarios(HorarioDTO horario) {
        try {
            if (repository.getHorario(horario.getHorarioInicial(), horario.getHorarioFinal()).isPresent())
                throw new HorarioAlreadyRegistered("Horário já cadastrado");
            if ( horario.getHorarioFinal().getHour() - horario.getHorarioInicial().getHour() < 1)
                throw new HorarioNotValidException("A diferença mínima entre o horário final e inicial é de 1 hora");
            if (horario.getHorarioInicial().isAfter(horario.getHorarioFinal()))
                throw new HorarioNotValidException("O horário final não pode ser posterior ao inicial");

        } catch (NullPointerException e) {
            throw new HorarioNotValidException("Os campos de horário são obrigatórios");
        }
    }

    // Define os turnos automaticamente com base nos horários informados
    private String defineTurno(HorarioDTO horarioDTO) {
        if (horarioDTO.getHorarioInicial().getHour() >= 6 && horarioDTO.getHorarioFinal().getHour() <= 12)
            return Turno.MANHA.toString();
        if (horarioDTO.getHorarioInicial().getHour() >= 12 && horarioDTO.getHorarioFinal().getHour() <= 18)
            return Turno.TARDE.toString();
        else
            return Turno.NOITE.toString();
    }

    private String validateTurno(String turno) {
        try {
            return Turno.valueOf(turno.toUpperCase()).toString();
        } catch (Exception e) {
            throw new ResourceNotExistingException(String.format("Os turnos existentes são: %s", Arrays.toString(Turno.values())));
        }
    }
}
