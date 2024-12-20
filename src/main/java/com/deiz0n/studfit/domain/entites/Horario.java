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
    @Column(name = "horario_inicial")
    private LocalTime horarioInicial;
    @Column(name = "horario_final")
    private LocalTime horarioFinal;
    private String turno;
    @Column(name = "vagas_disponiveis")
    private Integer vagasDisponiveis;

    @OneToMany(mappedBy = "horario")
    private List<Aluno> alunos;

}
