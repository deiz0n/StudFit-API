package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface HorarioRepository extends JpaRepository<Horario, UUID> {

    @Query("FROM Horario h WHERE h.horarioInicial = :start AND h.horarioFinal = :end")
    Optional<Horario> getHorario(LocalTime start, LocalTime end);

}
