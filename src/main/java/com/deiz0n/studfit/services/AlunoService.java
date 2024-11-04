package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Presenca;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private AlunoRepository alunoRepository;
    private PresencaRepository presencaRepository;
    private ModelMapper mapper;
    private ApplicationEventPublisher eventPublisher;
    private HorarioRepository horarioRepository;
    private Integer quantityAusencias = 0;

    public AlunoService(AlunoRepository alunoRepository, PresencaRepository presencaRepository, ModelMapper mapper, ApplicationEventPublisher eventPublisher, HorarioRepository horarioRepository) {
        this.alunoRepository = alunoRepository;
        this.presencaRepository = presencaRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
        this.horarioRepository = horarioRepository;
    }

    // Retorna todos os alunos que estão na lista de espera
    public List<AlunoListaEsperaDTO> getListaDeEspera() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> mapper.map(aluno, AlunoListaEsperaDTO.class))
                .filter(AlunoListaEsperaDTO::getListaEspera)
                .collect(Collectors.toList());
    }

    // Registra um aluno na lista de espera
    public AlunoListaEsperaDTO registerListaEspera(AlunoListaEsperaDTO alunoListaEspera) {
        isExisting(alunoListaEspera.getEmail());
        alunoListaEspera.setColocacao(getCurrentColocacao());
        var aluno = mapper.map(alunoListaEspera, Aluno.class);
        alunoRepository.save(aluno);
        return alunoListaEspera;
    }

    // Remove um aluno da lista de espera
    public AlunoListaEsperaDTO removeListaEspera(UUID id) {
        var aluno = getById(id);
        alunoRepository.delete(aluno);

        reorderListaEspera(aluno);

        return mapper.map(aluno, AlunoListaEsperaDTO.class);
    }

    // Retorna todos os alunos já cadastrados na academia
    public List<AlunoDTO> getEfetivados() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> mapper.map(aluno, AlunoDTO.class))
                .filter(aluno -> !aluno.getListaEspera())
                .collect(Collectors.toList());
    }

    // Realiza o cadastro completo do aluno na lista de espera
    public AlunoDTO registerEfetivado(AlunoDTO aluno) {
        var alunoEfetivado = alunoRepository.getByColocacao(1).orElseThrow(
                () -> new AlunoNotFoundException("Aluno não encontrado")
        );
        isExisting(aluno, alunoEfetivado.getId());
        isAvailable(aluno.getHorario());
        BeanUtils.copyProperties(aluno, alunoEfetivado, "id", "nome", "email");

        reorderListaEspera(alunoEfetivado);
        alunoEfetivado.setColocacao(null);

        var vagasDisponiveisEvent = new HorarioRegisterVagasDisponiveisEvent(this, aluno.getHorario().getId());
        eventPublisher.publishEvent(vagasDisponiveisEvent);

        alunoRepository.save(alunoEfetivado);

        return mapper.map(alunoEfetivado, AlunoDTO.class);
    }

    // Remove aluno cadastrado
    public void removeEfetivado(UUID id) {
        var aluno = getById(id);
        alunoRepository.delete(aluno);
    }

    // Atualiza os dados do aluno cadastrado
    public AlunoDTO updateEfetivado(UUID id, AlunoDTO alunoDTO) {
        isExisting(alunoDTO, id);
        var aluno = getById(id);
        BeanUtils.copyProperties(alunoDTO, aluno, "id");
        alunoRepository.save(aluno);
        return mapper.map(aluno, AlunoDTO.class);
    }

    private Aluno getById(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(
                        () -> new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", id.toString()))
                );
    }

    public AlunoDTO getAlunoById(UUID id) {
        return mapper.map(getById(id), AlunoDTO.class);
    }

    // Verifica a existência de email ao cadastrar um aluno na lista de espera
    private void isExisting(String email) {
        if (alunoRepository.getByEmail(email).isPresent())
            throw new EmailAlreadyRegisteredException("Email já cadastrado");
    }

    // Verifica a existência de email ou telefone ao efetivar ou atualizar dados de um aluno
    private void isExisting(AlunoDTO aluno, UUID id) {
        var alunoByEmail = alunoRepository.getByEmail(aluno.getEmail());
        if (alunoByEmail.isPresent() && !alunoByEmail.get().getId().equals(id))
            throw new EmailAlreadyRegisteredException("Email já cadastrado");

        var alunoByTelefone = alunoRepository.getByTelefone(aluno.getTelefone());
        if (alunoByTelefone.isPresent() && !alunoByTelefone.get().getId().equals(id))
            throw new TelefoneAlreadyRegistered("Telefone já cadastradp");
    }

    private Integer getCurrentColocacao() {
        return Math.toIntExact(alunoRepository.findAll()
                .stream()
                .filter(Aluno::getListaEspera)
                .count() + 1
        );
    }

    // Reorganiza a lista de espera
    private void reorderListaEspera(Aluno aluno) {
        List<Aluno> listOfAlunos = new ArrayList<>(
                alunoRepository.findAll()
                .stream()
                .filter(Aluno::getListaEspera)
                .toList()
        );

        var lastAluno = listOfAlunos.get(listOfAlunos.size()-1);
        var currentColocacao = 0;

        if (!aluno.equals(lastAluno)) {
            for (Aluno x : listOfAlunos) {
                currentColocacao = x.getColocacao();
                if (aluno.getColocacao() < x.getColocacao()) {
                    x.setColocacao(currentColocacao-1);
                    alunoRepository.save(x);
                }
            }
        }
    }

    // Verifica se o horário informado está disponível
    private void isAvailable(HorarioDTO horario) {
        var getHorarioById = horarioRepository.findById(horario.getId())
                .orElseThrow(
                        () -> new HorarioNotFoundException(String.format("O horário com id: %s não foi encontrado", horario.getId().toString()))
                );

        if (getHorarioById.getVagasDisponiveis() == 0)
            throw new HorarioINotAvailableException("O horário informado atingiu o número máximo de alunos");
    }

    // Registra as ausências dos alunos
    @EventListener
    private void setAusencias(AlunoRegisterAusenciasEvent registerAusencias) {
        var aluno = getById(registerAusencias
                .getPresenca()
                .getAluno()
                .getId()
        );

        List<Presenca> ausenciasByAluno = presencaRepository.getPresencas(aluno.getId());

        if (ausenciasByAluno.isEmpty() || !ausenciasByAluno.get(ausenciasByAluno.size()-1).getPresente())
            quantityAusencias++;
        else {
            for (int i = 0; i < ausenciasByAluno.size() - 1; i++) {
                if (ausenciasByAluno.get(i).getPresente()) {
                    // Verifica se é sexta e o próximo dia é segunda
                    if (ausenciasByAluno.get(i).getData().getDayOfWeek() == DayOfWeek.FRIDAY && ausenciasByAluno.get(i + 1).getData().getDayOfWeek() == DayOfWeek.MONDAY)
                        quantityAusencias++;
                    // Verifica se as ausências estão em sequência
                    else if (ausenciasByAluno.get(i).getData().plusDays(1).equals(ausenciasByAluno.get(i + 1).getData()))
                        quantityAusencias++;
                    // Verifica se é o último dia do mês
                    else if (ausenciasByAluno.get(i).getData().getDayOfMonth() == ausenciasByAluno.get(i).getData().lengthOfMonth() && ausenciasByAluno.get(i + 1).getData().getDayOfMonth() == 1)
                        quantityAusencias++;
                } else {
                    // Zera a quantidade de ausêcias caso a frequência seja quebrada
                    quantityAusencias = 0;
                    break;
                }
            }
        }
        aluno.setAusenciasConsecutivas(quantityAusencias);
        alunoRepository.save(aluno);

        var registerStatus = new AlunoRegisterStatusEvent(this, aluno.getId());
        eventPublisher.publishEvent(registerStatus);

        var deletedAluno = new AlunoDeletedByAusenciasEvent(this, mapper.map(aluno, AlunoDTO.class));
        eventPublisher.publishEvent(deletedAluno);
    }

    @EventListener
    private void setStatus(AlunoRegisterStatusEvent registerStatusEvent) {
        var aluno = getById(registerStatusEvent.getId());
        if (aluno.getAusenciasConsecutivas() >= 3)
            aluno.setStatus(Status.EM_ALERTA);
        else
            aluno.setStatus(Status.REGULAR);
        alunoRepository.save(aluno);
    }

    @EventListener
    private void deleteByMaxAusencias(AlunoDeletedByAusenciasEvent deletedAlunoStatus) {
        var aluno = deletedAlunoStatus.getAluno();
        if (aluno.getAusenciasConsecutivas() == 5) {
            var findAluno = getById(aluno.getId());

            var deletedAluno = new SentEmailDeletedAlunoEfetivadoEvent(this, aluno.getEmail(), aluno.getNome());
            eventPublisher.publishEvent(deletedAluno);

            alunoRepository.deleteById(findAluno.getId());
        }
    }
}