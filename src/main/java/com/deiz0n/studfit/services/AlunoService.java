package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.domain.exceptions.AlunoNotFoundException;
import com.deiz0n.studfit.repositories.AlunoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private AlunoRepository alunoRepository;
    private ModelMapper mapper;

    public AlunoService(AlunoRepository alunoRepository, ModelMapper mapper) {
        this.alunoRepository = alunoRepository;
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

    // Retorna todos os alunos já cadastrados na academia
    public List<AlunoDTO> getEfetivados() {
        return alunoRepository.findAll()
                .stream()
                .map(aluno -> mapper.map(aluno, AlunoDTO.class))
                .filter(aluno -> !aluno.getListaEspera())
                .collect(Collectors.toList());
    }

    // Registra um aluno na lista de espera
    public AlunoListaEsperaDTO registerListaEspera(AlunoListaEsperaDTO alunoListaEspera) {
        alunoListaEspera.setColocacao(getColocacaoAtual());
        var aluno = mapper.map(alunoListaEspera, Aluno.class);
        alunoRepository.save(aluno);
        return alunoListaEspera;
    }

    public AlunoListaEsperaDTO removeListaEspera(UUID id) {
        var aluno = getById(id);
        alunoRepository.delete(aluno);

        reorderListaEspera(aluno);

        return new AlunoListaEsperaDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getColocacao(),
                aluno.getListaEspera()
        );
    }

    private Integer getColocacaoAtual() {
        return Math.toIntExact(alunoRepository.findAll()
                .stream()
                .filter(Aluno::getListaEspera)
                .count() + 1
        );
    }

    private void reorderListaEspera(Aluno aluno) {
        List<Aluno> listOfAlunos = new ArrayList<>(alunoRepository.findAll());
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

    private Aluno getById(UUID id) {
        return alunoRepository.findById(id)
                .orElseThrow(
                        () -> new AlunoNotFoundException(String.format("Aluno com ID: %s não foi encontrado", id.toString()))
                );
    }
}
