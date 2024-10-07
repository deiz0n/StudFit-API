package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HorarioRepository extends JpaRepository<Horario, UUID> {
}
