package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.enums.Turno;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TurnoDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;
    private Turno tipoTurno;

    public TurnoDTO(Turno tipoTurno) {
        this.tipoTurno = tipoTurno;
    }
}
