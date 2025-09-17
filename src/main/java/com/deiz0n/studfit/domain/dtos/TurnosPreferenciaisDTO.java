package com.deiz0n.studfit.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "TurnosPreferenciais", description = "Turnos preferenciais de um aluno")
public class TurnosPreferenciaisDTO {

    @Schema(description = "Turno preferencial do aluno", required = true)
    private TurnoDTO turno;

}
