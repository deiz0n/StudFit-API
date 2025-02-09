package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Horario;
import com.deiz0n.studfit.domain.enums.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HorarioRepository extends JpaRepository<Horario, UUID> {

    @Query("FROM tb_horario h WHERE h.horarioInicial = :start AND h.horarioFinal = :end")
    Optional<Horario> buscarHorario(LocalTime start, LocalTime end);

    @Query("FROM tb_horario h WHERE h.turno = :turno")
    List<Horario> buscarHorariosPorTurno(Turno turno);

}
