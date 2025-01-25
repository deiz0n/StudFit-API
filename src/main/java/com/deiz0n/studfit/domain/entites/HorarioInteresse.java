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
@Entity(name = "tb_horario_interesse")
public class HorarioInteresse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private AlunoHorarioPreferencia alunoHorarioPreferencia;
    @OneToMany(mappedBy = "horarioInteresse", fetch = FetchType.LAZY)
    private List<Horario> horarios;

}
