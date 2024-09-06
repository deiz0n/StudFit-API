package com.deiz0n.studfit.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Integer colocacao;
    private String nome;
    private Double peso;
    private Integer altura;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String telefone;
    private String cirurgias;
    private String patologias;
    @Column(name = "meses_experiencia_musculacao")
    private Integer mesesExperienciaMusculacao;
    @Column(name = "diagnostico_lesao_joelho")
    private String diagnosticoLesaoJoelho;
    private String status;
    @Column(name = "ausencias_consecutivas")
    private Integer ausenciasConsecutivas;
    private Boolean ativado;

    @OneToMany(mappedBy = "aluno")
    private List<Presenca> presencas;
    @OneToOne
    private Turno turno;

}
