package com.deiz0n.studfit.domain.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "nome", unique = true)
    private String nome;
    @Column(updatable = true, name = "updated_at")
    private Instant updatedAt;
    @Column(insertable = true, name = "created_at")
    private Instant createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "turno")
    private List<Horario> horarios;
    @JsonIgnore
    @OneToMany(mappedBy = "turno")
    private List<TurnosPreferenciais> turnosPreferenciais;
}
