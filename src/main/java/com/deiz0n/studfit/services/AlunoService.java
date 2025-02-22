package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.*;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.domain.entites.Usuario;
import com.deiz0n.studfit.domain.enums.Status;
import com.deiz0n.studfit.domain.events.*;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.horario.HorarioINotAvailableException;
import com.deiz0n.studfit.domain.exceptions.horario.HorarioNotFoundException;
import com.deiz0n.studfit.domain.exceptions.usuario.EmailAlreadyRegisteredException;
import com.deiz0n.studfit.domain.exceptions.usuario.TelefoneAlreadyRegistered;
import com.deiz0n.studfit.infrastructure.repositories.AlunoRepository;
import com.deiz0n.studfit.infrastructure.repositories.HorarioRepository;
import com.deiz0n.studfit.infrastructure.repositories.PresencaRepository;
import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PresencaRepository presencaRepository;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher eventPublisher;
    private final HorarioRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;

    public AlunoService(AlunoRepository alunoRepository, PresencaRepository presencaRepository, ModelMapper mapper, ApplicationEventPublisher eventPublisher, HorarioRepository horarioRepository, UsuarioRepository usuarioRepository) {
        this.alunoRepository = alunoRepository;
        this.presencaRepository = presencaRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
        this.horarioRepository = horarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Retorna todos os alunos que estão na lista de espera
    public List<AlunoListaEsperaDTO> buscarAlunosListaEspera(int numeroPagina, int quantidade) {
        var pageable = PageRequest.of(numeroPagina, quantidade);
        return alunoRepository.buscarAlunosListaEspera(pageable);
    }

    // Registra um aluno na lista de espera
    public AlunoListaEsperaDTO registrarAlunosListaEspera(AlunoListaEsperaDTO alunoListaEspera) {
        eExistente(alunoListaEspera.getEmail());
        alunoListaEspera.setColocacao(obterColocacaoAtual());
        var aluno = mapper.map(alunoListaEspera, Aluno.class);
        alunoRepository.save(aluno);
        return alunoListaEspera;
    }

    // Remove um aluno da lista de espera
    public AlunoListaEsperaDTO excluirAlunoListaEspera(UUID id) {
        var aluno = buscarPorId(id);
        alunoRepository.delete(aluno);

        reordenarListaEspera(aluno);

        return mapper.map(aluno, AlunoListaEsperaDTO.class);
    }

    // Retorna todos os alunos já cadastrados na academia
    public List<AlunoEfetivadoDTO> buscarAlunosEfetivados(int numeroPagina, int quantidade) {
        var pageable = PageRequest.of(numeroPagina, quantidade);
        return alunoRepository.buscarAlunosEfetivados(pageable);
    }

    // Realiza o cadastro completo do aluno na lista de espera
    public AlunoDTO registrarAlunoEfetivado(AlunoDTO aluno) {
        var alunoEfetivado = alunoRepository.buscarPorColocacao(1).orElseThrow(
                () -> new AlunoNotFoundException("Aluno não encontrado")
        );
        eExistente(aluno, alunoEfetivado.getId());
        estaDisponivel(aluno.getHorario());

        BeanUtils.copyProperties(aluno, alunoEfetivado, "id", "nome", "email");

        reordenarListaEspera(alunoEfetivado);
        alunoEfetivado.setColocacao(null);

        var vagasDisponiveisEvent = new HorarioRegisterVagasDisponiveisEvent(this, aluno.getHorario().getId());
        eventPublisher.publishEvent(vagasDisponiveisEvent);

        var sentAlunoEfetivado = new SentEmailAlunoEfetivadoEvent(this, new String[]{alunoEfetivado.getEmail()}, alunoEfetivado.getNome());
        eventPublisher.publishEvent(sentAlunoEfetivado);

        var listaDeDestinatarios = usuarioRepository.findAll()
                .stream()
                .map(Usuario::getEmail)
                .toArray(String[]::new);

        var sentAlunoEfetivadoToUsuarios = new SentAlunoEfetivadoToUsuarios(this, listaDeDestinatarios, alunoEfetivado.getNome());
        eventPublisher.publishEvent(sentAlunoEfetivadoToUsuarios);

        alunoRepository.save(alunoEfetivado);

        return mapper.map(alunoEfetivado, AlunoDTO.class);
    }

    // Remove aluno cadastrado
    public void excluirAlunoEfetivado(UUID id) {
        var aluno = buscarPorId(id);
        alunoRepository.delete(aluno);
    }

    // Atualiza os dados do aluno cadastrado
    public AlunoDTO atualizarEfetivado(UUID id, AlunoDTO alunoDTO) {
        eExistente(alunoDTO, id);
        var aluno = buscarPorId(id);
        BeanUtils.copyProperties(alunoDTO, aluno, "id");
        alunoRepository.save(aluno);
        return mapper.map(aluno, AlunoDTO.class);
    }

    private Aluno buscarPorId(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(
                        () -> new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", id))
                );
    }

    public AlunoDTO buscarAlunoPorId(UUID id) {
        return mapper.map(buscarPorId(id), AlunoDTO.class);
    }

    // Verifica a existência de email ao cadastrar um aluno na lista de espera
    private void eExistente(String email) {
        if (alunoRepository.buscarPorEmail(email).isPresent())
            throw new EmailAlreadyRegisteredException("Email já cadastrado");
    }

    // Verifica a existência de email ou telefone ao efetivar ou atualizar dados de um aluno
    private void eExistente(AlunoDTO aluno, UUID id) {
        var alunoPorEmail = alunoRepository.buscarPorEmail(aluno.getEmail());
        if (alunoPorEmail.isPresent() && !alunoPorEmail.get().getId().equals(id))
            throw new EmailAlreadyRegisteredException("Email já cadastrado");

        var alunoPorTelefone = alunoRepository.buscarPorTelefone(aluno.getTelefone());
        if (alunoPorTelefone.isPresent() && !alunoPorTelefone.get().getId().equals(id))
            throw new TelefoneAlreadyRegistered("Telefone já cadastrado");
    }

    private Integer obterColocacaoAtual() {
        return Math.toIntExact(alunoRepository.findAll()
                .stream()
                .filter(Aluno::getListaEspera)
                .count() + 1
        );
    }

    // Reorganiza a lista de espera
    private void reordenarListaEspera(Aluno aluno) {
        List<Aluno> listaAlunos = new ArrayList<>(
                alunoRepository.findAll()
                .stream()
                .filter(Aluno::getListaEspera)
                .toList()
        );

        var ultimoAluno = listaAlunos.get(listaAlunos.size()-1);
        var colocacaoAtual = 0;

        if (!aluno.equals(ultimoAluno)) {
            for (Aluno x : listaAlunos) {
                colocacaoAtual = x.getColocacao();
                if (aluno.getColocacao() < x.getColocacao()) {
                    x.setColocacao(colocacaoAtual-1);
                    alunoRepository.save(x);
                }
            }
        }
    }

    // Verifica se o horário informado está disponível
    private void estaDisponivel(HorarioDTO horario) {
        var buscarHorarioPorId = horarioRepository.findById(horario.getId())
                .orElseThrow(
                        () -> new HorarioNotFoundException(String.format("O horário com id: %s não foi encontrado", horario.getId().toString()))
                );

        if (buscarHorarioPorId.getVagasDisponiveis() == 0)
            throw new HorarioINotAvailableException("O horário informado atingiu o número máximo de alunos");
    }

    // Registra as ausências dos alunos
    @EventListener
    private void atualizarAusencias(AlunoRegisterAusenciasEvent registerAusencias) {
        var aluno = buscarPorId(registerAusencias
                .getPresenca()
                .getAluno()
                .getId()
        );

        int quantidadeAusencias = aluno.getAusenciasConsecutivas();

        List<Presenca> ausenciasPorAluno = presencaRepository.buscarUltimasPresencas(aluno.getId());

        if (ausenciasPorAluno.isEmpty() || !ausenciasPorAluno.stream().findFirst().get().getPresente()) {
            quantidadeAusencias++;
        } else {
            for (int i = 0; i < ausenciasPorAluno.size() - 1; i++) {
                if (!ausenciasPorAluno.get(i).getPresente()) {
                    if (ausenciasPorAluno.get(i).getData().getDayOfWeek() == DayOfWeek.FRIDAY && ausenciasPorAluno.get(i + 1).getData().getDayOfWeek() == DayOfWeek.MONDAY)
                        quantidadeAusencias++;
                        // Verifica se as ausências estão em sequência
                    else if (ausenciasPorAluno.get(i).getData().plusDays(1).equals(ausenciasPorAluno.get(i + 1).getData()))
                        quantidadeAusencias++;
                        // Verifica se é o último dia do mês
                    else if (ausenciasPorAluno.get(i).getData().getDayOfMonth() == ausenciasPorAluno.get(i).getData().lengthOfMonth() && ausenciasPorAluno.get(i + 1).getData().getDayOfMonth() == 1)
                        quantidadeAusencias++;
                } else {
                    quantidadeAusencias = 0;
                    break;
                }
            }
        }

        aluno.setAusenciasConsecutivas(quantidadeAusencias);
        alunoRepository.save(aluno);

        var registerStatus = new AlunoRegisterStatusEvent(this, aluno.getId());
        eventPublisher.publishEvent(registerStatus);

        var deletedAluno = new AlunoDeletedByAusenciasEvent(this, mapper.map(aluno, AlunoDTO.class));
        eventPublisher.publishEvent(deletedAluno);
    }

    @EventListener
    private void atualizarStatusAluno(AlunoRegisterStatusEvent registerStatusEvent) {
        var aluno = buscarPorId(registerStatusEvent.getId());
        if (aluno.getAusenciasConsecutivas() >= 3)
            aluno.setStatus(Status.EM_ALERTA);
        else
            aluno.setStatus(Status.REGULAR);
        alunoRepository.save(aluno);
    }

    @EventListener
    private void excluirPorMaxAusencias(AlunoDeletedByAusenciasEvent deletedAlunoStatus) {
        var aluno = deletedAlunoStatus.getAluno();
        if (aluno.getAusenciasConsecutivas() == 5) {
            var buscarAluno = buscarPorId(aluno.getId());

            var alunoExcluido = new SentEmailDeletedAlunoEfetivadoEvent(this, new String[]{aluno.getEmail()}, aluno.getNome());
            eventPublisher.publishEvent(alunoExcluido);

            var listaDeUsuarios = usuarioRepository.findAll()
                    .stream()
                    .map(usuario -> mapper.map(usuario, UsuarioDTO.class))
                    .toList();

            var deletedToUsuarios = new SentAlunoDeletedToUsuariosEvent(this, listaDeUsuarios, aluno.getNome());
            eventPublisher.publishEvent(deletedToUsuarios);

            alunoRepository.deleteById(buscarAluno.getId());
        }
    }
}