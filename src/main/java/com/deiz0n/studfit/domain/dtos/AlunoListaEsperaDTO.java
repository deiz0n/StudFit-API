package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "AlunoListaEspera", description = "Dados de um aluno para cadastro na lista de espera")
public class AlunoListaEsperaDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único do aluno na lista de espera", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotBlank(message = "O campo \"nome\" é obrigatório")
    @Schema(description = "Nome completo do aluno", example = "Maria Silva", required = true)
    private String nome;

    @NotBlank(message = "O campo \"email\" é obrigatório")
    @Email(message = "Email inválido. Tente novamente")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "E-mail do aluno", example = "maria.silva@email.com", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Telefone do aluno", example = "11987654321", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String telefone;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Posição do aluno na fila de espera", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer colocacao;

    @JsonIgnore
    @Schema(hidden = true)
    private Boolean listaEspera = true;

    @Size(max = 2, message = "É possível selecionar até 2 turnos preferenciais")
    @Schema(description = "Lista de turnos preferenciais do aluno (máximo 2)", maxLength = 2, required = true)
    private List<TurnosPreferenciaisDTO> turnosPreferenciais;
    
}
