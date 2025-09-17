package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Turno", description = "Dados de um turno de funcionamento da academia")
public class TurnoDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "ID único do turno", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Schema(description = "Nome do turno", example = "MANHA", allowableValues = {"MANHA", "TARDE", "NOITE"}, required = true)
    private String nome;

    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora da última atualização", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;

    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    public TurnoDTO(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
