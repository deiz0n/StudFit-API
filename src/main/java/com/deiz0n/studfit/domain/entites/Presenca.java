package com.deiz0n.studfit.domain.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
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
    @Column(updatable = true, name = "updated_at")
    private Instant updatedAt;
    @Column(insertable = true, name = "created_at")
    private Instant createdAt;

    @ManyToOne
    private Aluno aluno;
    @ManyToOne
    private Usuario usuario;
}
