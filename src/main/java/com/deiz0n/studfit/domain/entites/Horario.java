package com.deiz0n.studfit.domain.entites;

import com.deiz0n.studfit.domain.enums.Turno;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity(name = "tb_horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "horario_inicial")
    private LocalTime horarioInicial;
    @Column(name = "horario_final")
    private LocalTime horarioFinal;
    @Enumerated(EnumType.STRING)
    private Turno turno;
    @Column(name = "vagas_disponiveis")
    private Integer vagasDisponiveis;

    @JsonIgnore
    @ManyToOne
    private HorarioInteresse horarioInteresse;
    @OneToMany(mappedBy = "horario", fetch = FetchType.LAZY)
    private List<Aluno> alunos;

}
