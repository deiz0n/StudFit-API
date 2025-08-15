package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.*;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.domain.entites.TurnosPreferenciais;
import com.deiz0n.studfit.domain.enums.Status;
import com.deiz0n.studfit.domain.events.*;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.horario.TurnoNotExistentException;
import com.deiz0n.studfit.domain.exceptions.usuario.EmailAlreadyRegisteredException;
import com.deiz0n.studfit.domain.exceptions.usuario.TelefoneAlreadyRegisteredException;
import com.deiz0n.studfit.infrastructure.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PresencaRepository presencaRepository;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher eventPublisher;
    private final HorarioRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;

    public AlunoService(AlunoRepository alunoRepository, PresencaRepository presencaRepository, ModelMapper mapper, ApplicationEventPublisher eventPublisher, HorarioRepository horarioRepository, UsuarioRepository usuarioRepository, TurnoRepository turnoRepository) {
        this.alunoRepository = alunoRepository;
        this.presencaRepository = presencaRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
        this.horarioRepository = horarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.turnoRepository = turnoRepository;
    }

    public List<AlunoListaEsperaDTO> buscarAlunosListaEspera(int numeroPagina, int quantidade, String turno) {
        TurnoDTO turnoDTO = turnoRepository
                .buscarPorNome(turno)
                .orElseThrow(
                        () -> new TurnoNotExistentException(String.format("O turno: %s não foi encontrado", turno))
                );

        List<AlunoListaEsperaDTO> alunos = alunoRepository.buscarAlunosListaEspera(turnoDTO.getNome())
                .stream()
                .map(aluno -> mapper.map(aluno, AlunoListaEsperaDTO.class))
                .toList();

        int inicio = numeroPagina * quantidade;
        int fim = Math.min(inicio + quantidade, alunos.size());

        if (inicio > alunos.size()) return new ArrayList<>();

        return alunos.subList(inicio, fim);
    }

    // Registra um aluno na lista de espera
    public AlunoListaEsperaDTO registrarAlunosListaEspera(AlunoListaEsperaDTO alunoListaEspera) {
        eExistente(alunoListaEspera.getEmail(), alunoListaEspera.getTelefone());

        if (!alunoListaEspera.getTurnosPreferenciais().isEmpty()) {
            var turno = alunoListaEspera.getTurnosPreferenciais().get(0).toString();
            alunoListaEspera.setColocacao(alunoRepository.quantidadeAlunosPorTurno(turno) + 1);
        } else {
            var turno = turnoRepository.buscarTodos().get(0);
            alunoListaEspera.setColocacao(alunoRepository.quantidadeAlunosPorTurno(turno.getNome()) + 1);
        }

        var aluno = mapper.map(alunoListaEspera, Aluno.class);
        alunoRepository.save(aluno);
        alocarAlunoListaEsperaEmHorario();
        return alunoListaEspera;
    }

    public void excluirAlunoListaEspera(UUID id) {
        var aluno = buscarPorId(id);
        alunoRepository.deleteById(aluno.getId());

        reordenarListaEspera(aluno.getId());
    }

    // Retorna todos os alunos já cadastrados na academia
    public List<AlunoEfetivadoDTO> buscarAlunosEfetivados(int numeroPagina, int quantidade) {
        var pageable = PageRequest.of(numeroPagina, quantidade);
        return alunoRepository.buscarAlunosEfetivados(pageable);
    }

    private void alocarAlunoListaEsperaEmHorario() {
        turnoRepository.findAll().forEach(turno -> {
            Optional<Aluno> alunoListaEspera = alunoRepository
                    .buscarAlunosPorTurno(turno.getNome())
                    .stream()
                    .findFirst();

            if (alunoListaEspera.isEmpty()) return;

            Aluno aluno = mapper.map(alunoListaEspera.get(), Aluno.class);

            Optional<Horario> horarioEscolhido = Optional.empty();

            if (aluno.getTurnosPreferenciais() != null && !aluno.getTurnosPreferenciais().isEmpty()) {
                for (TurnosPreferenciais preferencia : aluno.getTurnosPreferenciais()) {
                    horarioEscolhido = horarioRepository
                            .buscarHorariosPorTurno(preferencia.getTurno().getNome())
                            .stream()
                            .filter((horario -> horario.getVagasDisponiveis() > 0))
                            .findFirst();
                    if (horarioEscolhido.isPresent()) break;
                }
            }

            if (horarioEscolhido.isEmpty()) {
                horarioEscolhido = horarioRepository.findAll()
                        .stream()
                        .filter(horario -> horario.getVagasDisponiveis() > 0)
                        .findFirst();
            }

            if (horarioEscolhido.isPresent()) {
                var horario = horarioEscolhido.get();
                horario.setVagasDisponiveis(horario.getVagasDisponiveis() - 1);
                horarioRepository.save(horario);

                aluno.setHorario(horario);
                aluno.setListaEspera(false);
                aluno.setColocacao(null);
                aluno.setStatus(Status.CADASTRO_PENDENTE);
                alunoRepository.save(aluno);

                reordenarListaEspera(aluno.getId());
            }
        });
    }

    // Remove aluno cadastrado
    public void excluirAlunoEfetivado(UUID id) {
        var aluno = buscarPorId(id);
        alunoRepository.deleteById(aluno.getId());
    }

    // Atualiza os dados do aluno cadastrado
    public AlunoDTO atualizarEfetivado(UUID id, AlunoDTO alunoDTO) {
        eExistente(alunoDTO, id);
        var aluno = buscarPorId(id);
        var alunoAtualizado = mapper.map(alunoDTO, Aluno.class);
        alunoRepository.save(alunoAtualizado);
        return mapper.map(aluno, AlunoDTO.class);
    }

    public AlunoDTO buscarPorId(UUID id) {
        return alunoRepository.findById(id)
                .map(aluno -> mapper.map(aluno, AlunoDTO.class))
                .orElseThrow(
                        () -> new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", id))
                );
    }

    // Verifica a existência de email ao cadastrar um aluno na lista de espera
    private void eExistente(String email, String telefone) {
        if (alunoRepository.buscarPorEmail(email).isPresent())
            throw new EmailAlreadyRegisteredException("Email já cadastrado");

        if (alunoRepository.buscarPorTelefone(telefone).isPresent())
            throw new TelefoneAlreadyRegisteredException("Telefone já cadastrado");
    }

    // Verifica a existência de email ou telefone ao efetivar ou atualizar dados de um aluno
    private void eExistente(AlunoDTO aluno, UUID id) {
        var alunoPorEmail = alunoRepository.buscarPorEmail(aluno.getEmail());
        if (alunoPorEmail.isPresent() && !alunoPorEmail.get().getId().equals(id))
            throw new EmailAlreadyRegisteredException("Email já cadastrado");

        var alunoPorTelefone = alunoRepository.buscarPorTelefone(aluno.getTelefone());
        if (alunoPorTelefone.isPresent() && !alunoPorTelefone.get().getId().equals(id))
            throw new TelefoneAlreadyRegisteredException("Telefone já cadastrado");
    }

    // Reorganiza a lista de espera
    private void reordenarListaEspera(UUID id) {
        var alunoEfetivado = this.buscarPorId(id);
        var turno = alunoEfetivado.getHorario().getTurno().getNome();
        var listaAlunos = alunoRepository.buscarAlunosPorTurno(turno);

        if (listaAlunos.isEmpty()) return;

        listaAlunos.sort(Comparator.comparingInt(Aluno::getColocacao));

        int colocacao = 1;
        for (Aluno aluno : listaAlunos) {
            aluno.setColocacao(colocacao++);
        }
        alunoRepository.saveAll(listaAlunos);
    }

    // Registra as ausências dos alunos
    @EventListener
    protected void atualizarAusencias(RegistrarAusenciasAlunoEvent registarAusencias) {
        var aluno = buscarPorId(registarAusencias
                .presenca()
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
                    if (
                            ausenciasPorAluno.get(i).getData().getDayOfWeek() == DayOfWeek.FRIDAY
                                    && ausenciasPorAluno.get(i + 1).getData().getDayOfWeek() == DayOfWeek.MONDAY
                    )
                        quantidadeAusencias++;
                        // Verifica se as ausências estão em sequência
                    else if (ausenciasPorAluno.get(i).getData().plusDays(1).equals(ausenciasPorAluno.get(i + 1).getData()))
                        quantidadeAusencias++;
                        // Verifica se é o último dia do mês
                    else if (
                            ausenciasPorAluno.get(i).getData().getDayOfMonth() == ausenciasPorAluno.get(i).getData().lengthOfMonth()
                                    && ausenciasPorAluno.get(i + 1).getData().getDayOfMonth() == 1
                    )
                        quantidadeAusencias++;
                } else {
                    quantidadeAusencias = 0;
                    break;
                }
            }
        }

        aluno.setAusenciasConsecutivas(quantidadeAusencias);
        alunoRepository.save(mapper.map(aluno, Aluno.class));

        var atualizarStatus = new AtualizarStatusAlunoEvent(aluno.getId());
        eventPublisher.publishEvent(atualizarStatus);

        var alunoExcluido = new ExclusaoAlunoPorAusenciasEvent(mapper.map(aluno, AlunoDTO.class));
        eventPublisher.publishEvent(alunoExcluido);
    }

    @EventListener
    protected void atualizarStatusAluno(AtualizarStatusAlunoEvent registerStatusEvent) {
        var aluno = buscarPorId(registerStatusEvent.id());
        if (aluno.getAusenciasConsecutivas() >= 3)
            aluno.setStatus(Status.EM_ALERTA);
        else
            aluno.setStatus(Status.REGULAR);
        alunoRepository.save(mapper.map(aluno, Aluno.class));
    }

    @EventListener
    protected void excluirPorMaxAusencias(ExclusaoAlunoPorAusenciasEvent deletedAlunoStatus) {
        var aluno = deletedAlunoStatus.aluno();
        if (aluno.getAusenciasConsecutivas() == 5) {
            var buscarAluno = buscarPorId(aluno.getId());

            var notificarAlunoCadastroExcluido = new NotificarAlunoCadastroExcluidoEvent(new String[]{aluno.getEmail()}, aluno.getNome());
            eventPublisher.publishEvent(notificarAlunoCadastroExcluido);

            var listaDeUsuarios = usuarioRepository.findAll()
                    .stream()
                    .map(usuario -> mapper.map(usuario, UsuarioDTO.class))
                    .toList();

            var notificarUsuarioCadastroExcluido = new NotificarUsuarioCadastroExcluidoEvent(listaDeUsuarios, aluno.getNome());
            eventPublisher.publishEvent(notificarUsuarioCadastroExcluido);

            alunoRepository.deleteById(buscarAluno.getId());
        }
    }
}