package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.infrastructure.repositories.PresencaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
