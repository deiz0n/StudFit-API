package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.domain.exceptions.AlunoNotEfetivadoException;
import com.deiz0n.studfit.domain.exceptions.AlunoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.UsuarioNotFoundException;
import com.deiz0n.studfit.infrastructure.repositories.AlunoRepository;
import com.deiz0n.studfit.infrastructure.repositories.PresencaRepository;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PresencaService {

    private PresencaRepository presencaRepository;
    private AlunoRepository alunoRepository;
    private UsuarioRepository usuarioRepository;
    private ModelMapper mapper;

    public PresencaService(PresencaRepository presencaRepository, AlunoRepository alunoRepository, UsuarioRepository usuarioRepository, ModelMapper mapper) {
        this.presencaRepository = presencaRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public List<PresencaDTO> getAll() {
        return presencaRepository.findAll()
                .stream()
                .map(presenca -> mapper.map(presenca, PresencaDTO.class))
                .collect(Collectors.toList());
    }

    public PresencaDTO create(PresencaDTO presencaDTO, LocalDate data) {
        isRegistered(presencaDTO);
        isEfetivado(presencaDTO);

        var presenca = mapper.map(presencaDTO, Presenca.class);

        presenca.setData(data);
        presencaRepository.save(presenca);

        return PresencaDTO.builder()
                .id(presenca.getId())
                .data(presenca.getData())
                .presente(presenca.getPresente())
                .aluno(mapper.map(presenca.getAluno(), AlunoDTO.class))
                .usuario(mapper.map(presenca.getUsuario(), UsuarioDTO.class))
                .build();
    }

    private void isRegistered(PresencaDTO presenca) {
        var aluno = alunoRepository.findById(presenca.getAluno().getId());
        if (aluno.isEmpty())
            throw new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", presenca.getAluno().getId().toString()));
        var usuario = usuarioRepository.findById(presenca.getUsuario().getId());
        if (usuario.isEmpty())
            throw new UsuarioNotFoundException(String.format("Usuário com ID: %s não foi encontrado", presenca.getUsuario().getId().toString()));
    }

    private void isEfetivado(PresencaDTO presenca) {
        var aluno = alunoRepository.findById(presenca.getAluno().getId()).get();

        if (aluno.getListaEspera())
            throw new AlunoNotEfetivadoException(String.format("Aluno com ID: %s está na lista de espera", presenca.getAluno().getId().toString()));
    }
}
