package com.deiz0n.studfit.domain.entites;

import com.deiz0n.studfit.domain.entites.turnosPrefenciaisId.TurnosPreferenciaisId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_turnos_preferenciais")
public class TurnosPreferenciais {

    @EmbeddedId
    private TurnosPreferenciaisId id;

    @JsonIgnore
    @ManyToOne @MapsId("alunoId")
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    @ManyToOne @MapsId("turnoId")
    @JoinColumn(name = "turno_id")
    private Turno turno;
}
