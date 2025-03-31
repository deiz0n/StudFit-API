package com.deiz0n.studfit.domain.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private com.deiz0n.studfit.domain.enums.Turno tipoTurno;

    @OneToMany(mappedBy = "turnoHorario")
    private List<Horario> horarios;
}
