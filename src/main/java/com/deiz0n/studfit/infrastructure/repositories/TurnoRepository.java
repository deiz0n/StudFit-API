package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TurnoRepository extends JpaRepository<Turno, UUID> {
    
    List<Turno> findByTipoTurno(com.deiz0n.studfit.domain.enums.Turno tipoTurno);

}
