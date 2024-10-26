package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.enums.Turno;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioDTO implements Comparable<HorarioDTO> {

    private UUID id;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("horario_inicial")
    private LocalTime horarioInicial;
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("horario_final")
    private LocalTime horarioFinal;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Turno turno;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "vagas_disponiveis", access = JsonProperty.Access.READ_ONLY)
    private Integer vagasDisponiveis;

    @Override
    public int compareTo(HorarioDTO horario) {
        if (this.horarioInicial.isAfter(horario.getHorarioInicial()) && getHorarioFinal().isAfter(horario.getHorarioFinal()))
            return 1;
        if (horario.getHorarioInicial().isAfter(this.horarioInicial) && horario.getHorarioFinal().isAfter(this.getHorarioFinal()))
            return -1;
        return 0;
    }
}
