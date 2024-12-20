package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.domain.events.AlunoRegisterAusenciasEvent;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotEfetivadoException;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.presenca.PresencaAlreadyRegistered;
import com.deiz0n.studfit.domain.exceptions.presenca.PresencaNotValidException;
import com.deiz0n.studfit.domain.exceptions.usuario.UsuarioNotFoundException;
import com.deiz0n.studfit.infrastructure.repositories.AlunoRepository;
import com.deiz0n.studfit.infrastructure.repositories.PresencaRepository;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresencaService {

    private PresencaRepository presencaRepository;
    private AlunoRepository alunoRepository;
    private UsuarioRepository usuarioRepository;
    private ModelMapper mapper;
    private ApplicationEventPublisher eventPublisher;

    public PresencaService(PresencaRepository presencaRepository, AlunoRepository alunoRepository, UsuarioRepository usuarioRepository, ModelMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.presencaRepository = presencaRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    public List<PresencaDTO> getAll() {
        return presencaRepository.findAll()
                .stream()
                .map(presenca -> mapper.map(presenca, PresencaDTO.class))
                .collect(Collectors.toList());
    }

    public List<PresencaDTO> create(List<PresencaDTO> presencaDTO, LocalDate data) {
        var presenca = presencaDTO.stream()
                        .map(dto -> mapper.map(dto, Presenca.class))
                        .toList();

        presenca.forEach(entity -> {
            entity.setData(data);
            validatePresenca(entity);
        });

        presencaRepository.saveAll(presenca);

        presencaDTO.forEach(dto -> {
            eventPublisher.publishEvent(new AlunoRegisterAusenciasEvent(this, dto));
        });

        return presenca.stream()
                .map(entity -> mapper.map(entity, PresencaDTO.class))
                .toList();
    }

    private void validatePresenca(Presenca presenca) {
        // Verifica se o aluno e o usuário estão cadastrados
        var aluno = alunoRepository.findById(presenca.getAluno().getId());
        if (aluno.isEmpty())
            throw new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", presenca.getAluno().getId().toString()));
        var usuario = usuarioRepository.findById(presenca.getUsuario().getId());
        if (usuario.isEmpty())
            throw new UsuarioNotFoundException(String.format("Usuário com ID: %s não foi encontrado", presenca.getUsuario().getId().toString()));

        if (aluno.get().getListaEspera()) // Verifica se o aluno estar efetivado
            throw new AlunoNotEfetivadoException(String.format("Aluno com ID: %s está na lista de espera", presenca.getAluno().getId().toString()));

        var presencaByData = presencaRepository.getFirstByData(presenca.getData()); // Verifica se a presença é existente
        if (presencaByData.isPresent())
            throw new PresencaAlreadyRegistered("Presenca já realizada");
        if (presenca.getData().getDayOfWeek() == DayOfWeek.SATURDAY || presenca.getData().getDayOfWeek() == DayOfWeek.SUNDAY) // Verifica se a presença foi realizada em final de semana
            throw new PresencaNotValidException("Presença não pode ser realizada em sábados e domingos");
        if (!presenca.getData().equals(LocalDate.now()))
            if (presenca.getData().isBefore(LocalDate.now()))
                throw new PresencaNotValidException("Presença não pode ser realizada em datas anteriores");
//            else
//                throw new PresencaNotValidException("Presença não pode ser realizada em datas posteriores");
    }
}
