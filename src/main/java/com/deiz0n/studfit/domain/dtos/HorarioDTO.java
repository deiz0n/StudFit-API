package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.enums.Turno;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioDTO {

    private UUID id;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("horario_inicial")
    private LocalTime horarioInicial;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("horario_final")
    private LocalTime horarioFinal;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Turno turno;

}
