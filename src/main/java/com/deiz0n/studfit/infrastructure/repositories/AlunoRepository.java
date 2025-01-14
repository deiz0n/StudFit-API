package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    @Query("FROM Aluno a WHERE a.colocacao = :colocacao")
    Optional<Aluno> buscarPorColocacao(Integer colocacao);
    @Query("FROM Aluno a WHERE a.email = :email")
    Optional<Aluno> buscarPorEmail(String email);
    @Query("FROM Aluno a WHERE a.telefone = :telefone")
    Optional<Aluno> buscarPorTelefone(String telefone);
    @Query("FROM Aluno a WHERE a.listaEspera = true")
    List<Aluno> buscarAlunosListaEspera();
}