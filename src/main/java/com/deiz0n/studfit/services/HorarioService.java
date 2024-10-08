package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.infrastructure.repositories.HorarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .collect(Collectors.toList());
    }

    public HorarioDTO create(HorarioDTO horarioDTO) {
        var horario = mapper.map(horarioDTO, Horario.class);

        repository.save(horario);

        return HorarioDTO.builder()
                .id(horario.getId())
                .horarioInicial(horario.getHorarioInicial())
                .horarioFinal(horario.getHorarioFinal())
                .turno(horario.getTurno())
                .build();
    }
}
