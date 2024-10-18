package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.entites.Presenca;
import com.deiz0n.studfit.domain.events.AlunoRegisterAusenciasEvent;
import com.deiz0n.studfit.domain.exceptions.AlunoNotFoundException;
import com.deiz0n.studfit.domain.exceptions.EmailAlreadyRegisteredException;
import com.deiz0n.studfit.domain.exceptions.TelefoneAlreadyRegistered;
import com.deiz0n.studfit.infrastructure.repositories.AlunoRepository;
import com.deiz0n.studfit.infrastructure.repositories.PresencaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
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
    private Integer quantityAusencias = 0;

    public AlunoService(AlunoRepository alunoRepository, PresencaRepository presencaRepository, ModelMapper mapper) {
        this.alunoRepository = alunoRepository;
        this.presencaRepository = presencaRepository;
        this.mapper = mapper;
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
        BeanUtils.copyProperties(aluno, alunoEfetivado, "id", "nome", "email");

        reorderListaEspera(alunoEfetivado);
        alunoEfetivado.setColocacao(null);
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

    // Registra as ausências dos alunos
    @EventListener
    private int setAusencias(AlunoRegisterAusenciasEvent registerAusencias) {
        var aluno = getById(registerAusencias
                .getPresenca()
                .getAluno()
                .getId()
        );

        List<Presenca> presencasByAluno = presencaRepository.getPresencas(aluno.getId());

        if (presencasByAluno.isEmpty() || !presencasByAluno.get(presencasByAluno.size()-1).getPresente()) {
            quantityAusencias++;
            aluno.setAusenciasConsecutivas(quantityAusencias);
            alunoRepository.save(aluno);
        } else {
            for (int i = 0; i < presencasByAluno.size() - 1; i++) {
                if (presencasByAluno.get(i).getPresente()) {
                    if (presencasByAluno.get(i).getData().getDayOfWeek() == DayOfWeek.FRIDAY && presencasByAluno.get(i).getData().getDayOfWeek() == DayOfWeek.MONDAY) {
                        quantityAusencias++;
                        aluno.setAusenciasConsecutivas(quantityAusencias);
                        alunoRepository.save(aluno);
                    }
                    // Verifica se as ausências estão em sequência
                    else if (presencasByAluno.get(i).getData().plusDays(presencasByAluno.get(i + 1).getData().getDayOfMonth()).equals(presencasByAluno.get(i + 1).getData())) {
                        quantityAusencias++;
                        aluno.setAusenciasConsecutivas(quantityAusencias);
                        alunoRepository.save(aluno);
                    }
                    // Verifica se é o último dia do mês
                    else if (presencasByAluno.get(i).getData().getDayOfMonth() == presencasByAluno.get(i).getData().lengthOfMonth() && presencasByAluno.get(i + 1).getData().getDayOfMonth() == 1) {
                        quantityAusencias++;
                        aluno.setAusenciasConsecutivas(quantityAusencias);
                        alunoRepository.save(aluno);
                    }
                } else {
                    // Zera a quantidade de ausêcias caso a frequência seja quebrada
                    quantityAusencias = 0;
                    aluno.setAusenciasConsecutivas(quantityAusencias);
                    alunoRepository.save(aluno);
                    break;
                }
            }
        }
        return quantityAusencias;
    }
}


