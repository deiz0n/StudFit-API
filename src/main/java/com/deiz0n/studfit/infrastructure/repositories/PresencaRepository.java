package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PresencaRepository extends JpaRepository<Presenca, UUID> {

    @Query("SELECT p FROM Presenca p WHERE p.aluno.id = :id")
    List<Presenca> getPresencas(UUID id);

}
