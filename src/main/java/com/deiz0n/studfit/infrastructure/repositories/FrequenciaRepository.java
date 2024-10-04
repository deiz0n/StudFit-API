package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FrequenciaRepository extends JpaRepository<Presenca, UUID> {
}
