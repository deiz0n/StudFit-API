package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import com.deiz0n.studfit.repositories.AlunoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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
        alunoListaEspera.setColocacao(getColocaoAtual());
        var aluno = mapper.map(alunoListaEspera, Aluno.class);
        alunoRepository.save(aluno);
        return alunoListaEspera;
    }

    private Integer getColocaoAtual() {
        return Math.toIntExact(alunoRepository.count() + 1);
    }
}
