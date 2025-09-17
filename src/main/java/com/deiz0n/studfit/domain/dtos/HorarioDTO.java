package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Horario", description = "Dados de um horário de funcionamento da academia")
public class HorarioDTO implements Comparable<HorarioDTO> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "ID único do horário", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("horario_inicial")
    @NotBlank(message = "O campo horario_inicial é obrigatório")
    @Schema(description = "Horário de início no formato HH:mm", example = "08:00", required = true, pattern = "HH:mm")
    private LocalTime horarioInicial;

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("horario_final")
    @NotBlank(message = "O campo horario_final é obrigatório")
    @Schema(description = "Horário de fim no formato HH:mm", example = "09:00", required = true, pattern = "HH:mm")
    private LocalTime horarioFinal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "vagas_disponiveis", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Número de vagas disponíveis neste horário", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer vagasDisponiveis;

    @Schema(description = "Turno ao qual este horário pertence", required = true)
    private TurnoDTO turno;

    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora da última atualização", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;
    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora da criação do horário", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    public HorarioDTO(LocalTime horarioInicial, LocalTime horarioFinal, TurnoDTO turno) {
        this.horarioInicial = horarioInicial;
        this.horarioFinal = horarioFinal;
        this.turno = turno;
    }

    @Override
    public int compareTo(HorarioDTO horario) {
        if (this.horarioInicial.isAfter(horario.getHorarioInicial()) && getHorarioFinal().isAfter(horario.getHorarioFinal()))
            return 1;
        if (horario.getHorarioInicial().isAfter(this.horarioInicial) && horario.getHorarioFinal().isAfter(this.getHorarioFinal()))
            return -1;
        return 0;
    }
}
