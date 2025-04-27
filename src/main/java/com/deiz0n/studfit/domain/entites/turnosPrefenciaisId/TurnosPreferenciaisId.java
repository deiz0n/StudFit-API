package com.deiz0n.studfit.domain.entites.turnosPrefenciaisId;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@Embeddable
public class TurnosPreferenciaisId implements Serializable {

    private UUID alunoId;
    private UUID turnoId;

}
