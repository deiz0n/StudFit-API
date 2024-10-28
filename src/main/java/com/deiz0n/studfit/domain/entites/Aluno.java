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
    private Status status;
    @Column(name = "ausencias_consecutivas")
    private Integer ausenciasConsecutivas;
    @Column(name = "lista_espera")
    private Boolean listaEspera;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Presenca> presencas;
    @ManyToOne
    private Horario horario;

}
