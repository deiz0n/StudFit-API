package com.deiz0n.studfit.domain.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_presenca")
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDate data;
    private Boolean presente;

    @ManyToOne
    private Aluno aluno;
    @OneToOne
    private Usuario usuario;
}
