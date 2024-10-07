package com.deiz0n.studfit.domain.entites;

import com.deiz0n.studfit.domain.enums.Turno;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "horario_inicial")
    private LocalTime horarioInicial;
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "horario_final")
    private LocalTime horarioFinal;
    private Turno turno;

    @OneToMany(mappedBy = "horario")
    private List<Aluno> alunos;

}
