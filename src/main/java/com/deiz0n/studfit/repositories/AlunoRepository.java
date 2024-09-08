package com.deiz0n.studfit.repositories;

import com.deiz0n.studfit.domain.entites.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
}