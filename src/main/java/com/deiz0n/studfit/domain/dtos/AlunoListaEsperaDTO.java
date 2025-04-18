package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunoListaEsperaDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @NotBlank(message = "O campo \"nome\" é obrigatório")
    private String nome;
    @NotBlank(message = "O campo \"email\" é obrigatório")
    @Email(message = "Email inválido. Tente novamente")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String telefone;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer colocacao;
    @JsonIgnore
    private Boolean listaEspera = true;
    private String[] turnosPreferenciais;

    public AlunoListaEsperaDTO(UUID id, String nome, Integer colocacao, String[] turnosPreferenciais) {
        this.id = id;
        this.nome = nome;
        this.colocacao = colocacao;
        this.turnosPreferenciais = turnosPreferenciais;
    }

}
