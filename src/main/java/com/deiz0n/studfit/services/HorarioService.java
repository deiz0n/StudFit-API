package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.domain.enums.Turno;
import com.deiz0n.studfit.domain.exceptions.HorarioAlreadyRegistered;
import com.deiz0n.studfit.domain.exceptions.HorarioNotFoundException;
import com.deiz0n.studfit.domain.exceptions.HorarioNotValidException;
import com.deiz0n.studfit.infrastructure.repositories.HorarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
                .build();
    }

    public void delete(UUID id) {
        var horario = findByID(id);
        repository.delete(horario);
    }

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
    private Turno defineTurno(HorarioDTO horarioDTO) {
        if (horarioDTO.getHorarioInicial().getHour() >= 6 && horarioDTO.getHorarioFinal().getHour() <= 12)
            return Turno.MANHA;
        if (horarioDTO.getHorarioInicial().getHour() >= 12 && horarioDTO.getHorarioFinal().getHour() <= 18)
            return Turno.TARDE;
        else
            return Turno.NOITE;
    }
}
