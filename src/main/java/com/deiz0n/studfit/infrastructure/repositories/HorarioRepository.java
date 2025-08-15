package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.dtos.HorarioDTO;
import com.deiz0n.studfit.domain.entites.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HorarioRepository extends JpaRepository<Horario, UUID> {

    @Query("FROM tb_horario h WHERE h.horarioInicial = :start AND h.horarioFinal = :end")
    Optional<Horario> buscarHorario(LocalTime start, LocalTime end);

    @Query("SELECT h FROM tb_horario h JOIN FETCH h.turno t WHERE t.nome = :turno")
    List<Horario> buscarHorariosPorTurno(String turno);

    @Query("SELECT NEW com.deiz0n.studfit.domain.dtos.HorarioDTO(" +
            "h.horarioInicial, " +
            "h.horarioFinal, " +
            "NEW com.deiz0n.studfit.domain.dtos.TurnoDTO(" +
            "h.turno.id, " +
            "h.turno.nome)) " +
            "FROM tb_horario h")
    List<HorarioDTO> buscarTodosHorarios();

}
