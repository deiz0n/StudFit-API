package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.dtos.TurnoDTO;
import com.deiz0n.studfit.domain.entites.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TurnoRepository extends JpaRepository<Turno, UUID> {

    @Query("SELECT NEW " +
            "com.deiz0n.studfit.domain.dtos.TurnoDTO(t.id, t.nome) " +
            "FROM tb_turno t " +
            "WHERE t.nome ILIKE :nome")
    Optional<TurnoDTO> buscarPorNome(String nome);

    @Query("SELECT NEW com.deiz0n.studfit.domain.dtos.TurnoDTO(t.id, t.nome) FROM tb_turno t")
    List<TurnoDTO> buscarTodos();

}
