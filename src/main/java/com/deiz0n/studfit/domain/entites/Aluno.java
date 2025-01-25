package com.deiz0n.studfit.domain.entites;

import com.deiz0n.studfit.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Integer colocacao;
    @Column(length = 150)
    private String nome;
    private Double peso;
    private Integer altura;
    @Column(unique = true, length = 50)
    private String email;
    @Column(unique = true, length = 11)
    private String telefone;
    private String cirurgias;
    private String patologias;
    @Column(name = "meses_experiencia_musculacao")
    private Integer mesesExperienciaMusculacao;
    @Column(name = "diagnostico_lesao_joelho")
    private String diagnosticoLesaoJoelho;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "ausencias_consecutivas")
    private Integer ausenciasConsecutivas;
    @Column(name = "lista_espera")
    private Boolean listaEspera;
    @Column(name = "consumo_alcool")
    private Boolean consumoAlcool;
    @Column(name = "consumo_cigarro")
    private Boolean consumoCigarro;
    @Column(name = "pratica_exercicio_fisico")
    private Boolean praticaExercicioFisico;
    @Lob
    private byte[] atestado;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Presenca> presencas;
    @ManyToOne
    private AlunoInteressado alunoInteressado;

}
