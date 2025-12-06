package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.*;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.domain.entites.TurnosPreferenciais;
import com.deiz0n.studfit.domain.enums.Cargo;
import com.deiz0n.studfit.domain.enums.Status;
import com.deiz0n.studfit.domain.events.*;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotEfetivadoException;
import com.deiz0n.studfit.domain.exceptions.aluno.AlunoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.aluno.AtestadoNotSavedException;
import com.deiz0n.studfit.domain.exceptions.aluno.AtestadoNotValidException;
import com.deiz0n.studfit.domain.exceptions.horario.TurnoNotExistentException;
import com.deiz0n.studfit.domain.exceptions.usuario.EmailAlreadyRegisteredException;
import com.deiz0n.studfit.domain.exceptions.usuario.TelefoneAlreadyRegisteredException;
import com.deiz0n.studfit.domain.utils.MapearPropriedades;
import com.deiz0n.studfit.domain.utils.ProcessarAlocacaoAluno;
import com.deiz0n.studfit.domain.utils.ReordenarListaEspera;
import com.deiz0n.studfit.infrastructure.aws.S3Service;
import com.deiz0n.studfit.infrastructure.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Service s3Service;
    private final MapearPropriedades mapearPropriedades;
    private final ProcessarAlocacaoAluno processarAlocacaoAluno;
    private final ReordenarListaEspera reordenarListaEspera;

    public AlunoService(
            AlunoRepository alunoRepository,
            PresencaRepository presencaRepository,
            ModelMapper mapper,
            ApplicationEventPublisher eventPublisher,
            HorarioRepository horarioRepository,
            UsuarioRepository usuarioRepository,
            TurnoRepository turnoRepository,
            S3Service s3Service,
            MapearPropriedades mapearPropriedades,
            ProcessarAlocacaoAluno processarAlocacaoAluno,
            ReordenarListaEspera reordenarListaEspera
    ) {
        this.alunoRepository = alunoRepository;
        this.presencaRepository = presencaRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
        this.horarioRepository = horarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.turnoRepository = turnoRepository;
        this.s3Service = s3Service;
        this.mapearPropriedades = mapearPropriedades;
        this.processarAlocacaoAluno = processarAlocacaoAluno;
        this.reordenarListaEspera = reordenarListaEspera;
    }

    @Cacheable("alunoListaEspera")
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

        if (
                alunoListaEspera.getListaEspera() != null
                        && alunoListaEspera.getTurnosPreferenciais() != null
                        && !alunoListaEspera.getTurnosPreferenciais().isEmpty()
        ) {
            var turno = alunoListaEspera.getTurnosPreferenciais().get(0).toString();
            alunoListaEspera.setColocacao(alunoRepository.quantidadeAlunosPorTurno(turno) + 1);
        } else {
            var turno = turnoRepository.buscarTodos().get(0);
            alunoListaEspera.setColocacao(alunoRepository.quantidadeAlunosPorTurno(turno.getNome()) + 1);
        }

        var aluno = mapper.map(alunoListaEspera, Aluno.class);
        alunoRepository.save(aluno);
        alocarAlunoEmHorarioDisponivel(aluno);
        return alunoListaEspera;
    }

    public void excluirAlunoListaEspera(UUID id) {
        var aluno = buscarPorId(id);
        alunoRepository.deleteById(aluno.getId());
        reordenarListaEspera.reordenar(aluno.getId());
    }

    // Retorna todos os alunos já cadastrados na academia
    @Cacheable("alunosEfetivados")
    public List<AlunoEfetivadoDTO> buscarAlunosEfetivados(int numeroPagina, int quantidade, String turno, Status status) {
        var pageable = PageRequest.of(numeroPagina, quantidade);
        return alunoRepository.buscarAlunosEfetivados(pageable, turno, status);
    }

    private Optional<Horario> buscarHorarioDisponivel(Aluno aluno) {
        Optional<Horario> horarioEscolhido = Optional.empty();

        if (aluno.getTurnosPreferenciais() != null && !aluno.getTurnosPreferenciais().isEmpty()) {
            for (TurnosPreferenciais preferencia : aluno.getTurnosPreferenciais()) {
                horarioEscolhido = horarioRepository
                        .buscarHorariosPorTurno(preferencia.getTurno().getNome())
                        .stream()
                        .filter(horario -> horario.getVagasDisponiveis() > 0)
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

        return horarioEscolhido;
    }

    private void alocarAlunoEmHorarioDisponivel(Aluno aluno) {
        Optional<Horario> horarioEscolhido = buscarHorarioDisponivel(aluno);
       processarAlocacaoAluno.processar(horarioEscolhido, aluno);
    }

    // Verifica a existência de alunos na lista de espera a cada 1 hora
    @Scheduled(fixedDelay = 3000)
    void alocarAlunosEmHorariosDisponivel() {
        turnoRepository.findAll().forEach(turno -> {
            alunoRepository.buscarAlunosPorTurno(turno.getNome())
                    .stream()
                    .findFirst()
                    .ifPresent(alunoEntity -> {
                        var aluno = mapper.map(alunoEntity, Aluno.class);
                        Optional<Horario> horarioEscolhido = buscarHorarioDisponivel(aluno);

                        processarAlocacaoAluno.processar(horarioEscolhido, aluno);
                    });
        });
    }

    public void registarAlunoStatusPendente(UUID id, AlunoDTO alunoDTO) {
        var aluno = alunoRepository.findById(id)
                .orElseThrow(
                        () -> new AlunoNotFoundException(
                                String.format(
                                        "Aluno com ID: %s não foi encontrado", id)
                        ));
        mapearPropriedades.mapearPropriedadesAluno(aluno, alunoDTO);
        aluno.setStatus(Status.REGULAR);
        alunoRepository.save(aluno);
    }

    // Remove aluno cadastrado
    public void excluirAlunoEfetivado(UUID id) {
        var aluno = buscarPorId(id);
        var horarioId = aluno.getHorario().getId();
        var atualizarVagasDisponiveis = new AtualizarVagasDisponiveisHorarioEvent(horarioId, true);
        eventPublisher.publishEvent(atualizarVagasDisponiveis);
        alunoRepository.deleteById(aluno.getId());
    }

    // Atualiza os dados do aluno cadastrado
    public AlunoDTO atualizarEfetivado(UUID id, AtualizarAlunoDTO alunoDTO) {
        var dto = mapper.map(alunoDTO, AlunoDTO.class);
        eExistente(dto, id);
        var aluno = mapper.map(buscarPorId(id), Aluno.class);
        mapearPropriedades.mapearPropriedadesAluno(aluno, dto);
        alunoRepository.save(aluno);
        return mapper.map(aluno, AlunoDTO.class);
    }

//    @Transactional
    public AlunoDTO buscarPorId(UUID id) {
        return alunoRepository.findById(id)
                .map(aluno -> mapper.map(aluno, AlunoDTO.class))
                .orElseThrow(
                        () -> new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", id))
                );
    }

    public void adicionarAtestado(MultipartFile atestado, UUID id) {
        var aluno = mapper.map(buscarPorId(id), Aluno.class);
        var nomeArquivo = StringUtils.cleanPath(Objects.requireNonNull(atestado.getOriginalFilename()));

        if (aluno.getStatus() == null)
            throw new AlunoNotEfetivadoException(
                    String.format("O aluno de id: %s ainda não foi efetivado", id.toString())
            );

        if (!Objects.equals(atestado.getContentType(), "application/pdf"))
            throw new AtestadoNotValidException("O arquivo informado possui formato inválido. Apenas arquivos 'PDF' são aceitos");

        if (nomeArquivo.contains(".."))
            throw new AtestadoNotValidException("O arquivo informado possui nome inválido");

        try {
            s3Service.uploadAtestado(atestado, id);
        } catch (IOException e) {
            throw new AtestadoNotSavedException("Erro ao salvar atestado");
        }
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
                    .filter(usuario -> {
                        var cargo = Cargo.valueOf(usuario.getCargo());
                        return cargo == Cargo.INSTRUTOR || cargo == Cargo.ESTAGIARIO;
                    })
                    .toList();

            var notificarUsuarioCadastroExcluido = new NotificarUsuarioCadastroExcluidoEvent(listaDeUsuarios, aluno.getNome());
            eventPublisher.publishEvent(notificarUsuarioCadastroExcluido);

            var horario = buscarAluno.getHorario();
            eventPublisher.publishEvent(new AtualizarVagasDisponiveisHorarioEvent(horario.getId(), true));

            alunoRepository.deleteById(buscarAluno.getId());
        }
    }
}