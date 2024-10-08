package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    Optional<Aluno> getByColocacao(Integer colocacao);
    Optional<Aluno> getByEmail(String email);
    Optional<Aluno> getByTelefone(String telefone);

}