package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.entites.TurnosPreferenciais;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private List<TurnosPreferenciaisDTO> turnosPreferenciais;

    public AlunoListaEsperaDTO(UUID id, String nome, Integer colocacao) {
        this.id = id;
        this.nome = nome;
        this.colocacao = colocacao;
    }

    public AlunoListaEsperaDTO(UUID id, String nome, Integer colocacao, List<TurnosPreferenciaisDTO> turnosPreferenciais) {
        this.id = id;
        this.nome = nome;
        this.colocacao = colocacao;
        this.turnosPreferenciais = turnosPreferenciais;
    }

}
