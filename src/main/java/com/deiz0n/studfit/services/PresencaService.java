package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.PresencaDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.domain.entites.Usuario;
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
import java.util.Optional;

@Service
public class PresencaService {

    private final PresencaRepository presencaRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    public PresencaService(PresencaRepository presencaRepository, AlunoRepository alunoRepository, UsuarioRepository usuarioRepository, ModelMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.presencaRepository = presencaRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    public List<PresencaDTO> buscarPresencas() {
        return presencaRepository.buscarPresencas();
    }

    public List<PresencaDTO> registar(List<PresencaDTO> presencaDTO, LocalDate data) {
        List<Presenca> presenca = presencaDTO.stream()
                        .map(dto -> mapper.map(dto, Presenca.class))
                        .toList();

        presenca.forEach(entity -> {
            entity.setData(data);
            validarPresenca(entity);
            eventPublisher.publishEvent(new AlunoRegisterAusenciasEvent(this, mapper.map(entity, PresencaDTO.class)));
        });

        presencaRepository.saveAll(presenca);

        return presenca.stream()
                .map(entity -> mapper.map(entity, PresencaDTO.class))
                .toList();
    }

    public List<PresencaDTO> buscarPorData(LocalDate data) {
        return presencaRepository.buscarPresencaPorData(data);
    }

    private void validarPresenca(Presenca presenca) {
        // Verifica se o aluno e o usuário estão cadastrados
        Optional<Aluno> aluno = alunoRepository.findById(presenca.getAluno().getId());
        if (aluno.isEmpty())
            throw new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", presenca.getAluno().getId().toString()));

        Optional<Usuario> usuario = usuarioRepository.findById(presenca.getUsuario().getId());
        if (usuario.isEmpty())
            throw new UsuarioNotFoundException(String.format("Usuário com ID: %s não foi encontrado", presenca.getUsuario().getId().toString()));
        if (aluno.get().getListaEspera()) // Verifica se o aluno estar efetivado
            throw new AlunoNotEfetivadoException(String.format("Aluno com ID: %s está na lista de espera", presenca.getAluno().getId().toString()));

        Optional<Presenca> presencaPorData = presencaRepository.getFirstByData(presenca.getData()); // Verifica se a presença é existente
        if (presencaPorData.isPresent())
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
