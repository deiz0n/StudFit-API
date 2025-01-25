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
@Entity(name = "tb_aluno_horario_preferencia")
public class AlunoHorarioPreferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "alunoHorarioPreferencia", fetch = FetchType.LAZY)
    private List<AlunoInteressado> alunosInteressados;
    @OneToMany(mappedBy = "alunoHorarioPreferencia", fetch = FetchType.LAZY)
    private List<HorarioInteresse> horariosInteresse;
}
