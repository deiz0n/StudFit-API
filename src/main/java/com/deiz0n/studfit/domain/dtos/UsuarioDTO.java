package com.deiz0n.studfit.domain.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Schema(name = "Usuario", description = "Dados de um usuário do sistema")
public class UsuarioDTO {

    @Schema(description = "ID único do usuário", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "João Silva", required = true)
    private String nome;

    @Email(message = "Email inválido. Tente novamente")
    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com", required = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Senha do usuário", example = "senha123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String senha;

    @Schema(description = "Cargo do usuário no sistema", example = "ADMIN", allowableValues = {"ADMIN", "FUNCIONARIO"}, required = true)
    private String cargo;

    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora da última atualização", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;

    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

}
