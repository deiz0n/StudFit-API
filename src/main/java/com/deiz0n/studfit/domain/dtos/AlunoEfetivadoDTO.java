package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "AlunoEfetivado", description = "Dados básicos de um aluno efetivado para controle de presença")
public class AlunoEfetivadoDTO {

    @Schema(description = "ID único do aluno efetivado", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID id;

    @Schema(description = "Nome completo do aluno", example = "Carlos Eduardo", required = true)
    private String nome;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Horário em que o aluno está matriculado")
    private HorarioDTO horario;

    @Schema(description = "Status atual do aluno", example = "ATIVO", allowableValues = {"ATIVO", "INATIVO", "SUSPENSO"}, required = true)
    private Status status;

}
