package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Presenca", description = "Dados de presença de um aluno em uma data específica")
public class PresencaDTO {

    @Schema(description = "ID único da presença", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Data da presença no formato dd/MM/yyyy", example = "15/03/2024", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate data;

    @Schema(description = "Indica se o aluno estava presente", example = "true", required = true)
    private Boolean presente;

    @Schema(description = "Dados do aluno efetivado")
    private AlunoEfetivadoDTO aluno;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Usuário que registrou a presença", accessMode = Schema.AccessMode.WRITE_ONLY)
    private UsuarioDTO usuario;

    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora da última atualização", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;

    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    public PresencaDTO(UUID id, LocalDate data, Boolean presente, AlunoEfetivadoDTO aluno) {
        this.id = id;
        this.data = data;
        this.presente = presente;
        this.aluno = aluno;
    }
}
