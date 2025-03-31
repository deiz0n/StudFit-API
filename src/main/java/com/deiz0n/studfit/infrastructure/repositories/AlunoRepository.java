package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.dtos.AlunoEfetivadoDTO;
import com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO;
import com.deiz0n.studfit.domain.entites.Aluno;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    @Query("FROM tb_aluno a WHERE a.colocacao = :colocacao")
    Optional<Aluno> buscarPorColocacao(Integer colocacao);

    @Query("FROM tb_aluno a WHERE a.email = :email")
    Optional<Aluno> buscarPorEmail(String email);

    @Query("FROM tb_aluno a WHERE a.telefone = :telefone")
    Optional<Aluno> buscarPorTelefone(String telefone);

    @Query("SELECT NEW com.deiz0n.studfit.domain.dtos.AlunoListaEsperaDTO(a.id, a.nome, a.colocacao) FROM tb_aluno a WHERE a.listaEspera = true")
    List<AlunoListaEsperaDTO> buscarAlunosListaEspera(Pageable pageable);

    @Query("SELECT NEW " +
            "com.deiz0n.studfit.domain.dtos.AlunoEfetivadoDTO(a.id, a.nome, NEW com.deiz0n.studfit.domain.dtos.HorarioDTO(h.horarioInicial, h.horarioFinal, new com.deiz0n.studfit.domain.dtos.TurnoDTO(t.tipoTurno)))" +
            " FROM tb_aluno a JOIN FETCH tb_horario h ON a.horario.id = h.id JOIN FETCH tb_turno t ON h.turnoHorario.id = t.id")
    List<AlunoEfetivadoDTO> buscarAlunosEfetivados(Pageable pageable);
}