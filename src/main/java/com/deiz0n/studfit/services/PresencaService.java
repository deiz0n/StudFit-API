package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.infrastructure.repositories.PresencaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresencaService {

    private PresencaRepository repository;
    private ModelMapper mapper;

    public PresencaService(PresencaRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PresencaDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(presenca -> mapper.map(presenca, PresencaDTO.class))
                .collect(Collectors.toList());
    }

    public PresencaDTO create(PresencaDTO presencaDTO) {
        var presenca = mapper.map(presencaDTO, Presenca.class);

        presenca.setData(LocalDate.now());
        repository.save(presenca);

        return PresencaDTO.builder()
                .id(presenca.getId())
                .data(presenca.getData())
                .presente(presenca.getPresente())
                .aluno(mapper.map(presenca.getAluno(), AlunoDTO.class))
                .usuario(mapper.map(presenca.getUsuario(), UsuarioDTO.class))
                .build();
    }
}
