package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.dtos.AlunoEfetivadoDTO;
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

    @Query("SELECT a " +
            "FROM tb_aluno a " +
            "LEFT JOIN FETCH a.turnosPreferenciais tp " +
            "LEFT JOIN FETCH tp.turno t " +
            "LEFT JOIN FETCH a.horario h " +
            "ORDER BY a.colocacao")
    List<Aluno> buscarAlunosListaEspera();

    @Query("SELECT NEW com.deiz0n.studfit.domain.dtos.AlunoEfetivadoDTO(a.id, a.nome, " +
            "NEW com.deiz0n.studfit.domain.dtos.HorarioDTO(h.horarioInicial, h.horarioFinal, " +
            "NEW com.deiz0n.studfit.domain.dtos.TurnoDTO(t.id, t.nome)), a.status) " +
            "FROM tb_aluno a " +
            "JOIN tb_horario h " +
            "ON a.horario.id = h.id " +
            "JOIN tb_turno t " +
            "ON h.turno.id = t.id")
    List<AlunoEfetivadoDTO> buscarAlunosEfetivados(Pageable pageable);
}