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
@Entity(name = "tb_aluno_interessado")
public class AlunoInteressado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "alunoInteressado", fetch = FetchType.LAZY)
    private List<Aluno> aluno;
    @ManyToOne
    private AlunoHorarioPreferencia alunoHorarioPreferencia;
}
