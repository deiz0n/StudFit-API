package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PresencaRepository extends JpaRepository<Presenca, UUID> {

    Optional<Presenca> getFirstByData(LocalDate data);
    @Query("SELECT p FROM Presenca p WHERE p.aluno.id = :id ORDER BY p.data DESC")
    List<Presenca> getLastTwo(UUID id);

}
