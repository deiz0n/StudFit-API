package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AlunoDTO {

    private UUID id;
    @NotBlank(message = "O campo \"nome\" é obrigatório")
    private String nome;
    @NotBlank(message = "O campo \"peso\" é obrigatório")
    private Double peso;
    @NotBlank(message = "O campo \"altura\" é obrigatório")
    private Integer altura;
    @NotBlank(message = "O campo \"email\" é obrigatório")
    @Email(message = "Email inválido. Tente novamente")
    private String email;
    @NotBlank(message = "O campo \"telefone\" é obrigatório")
    private String telefone;
    private String cirurgias;
    private String patologias;
    @JsonProperty("meses_experiencia_musculacao")
    private Integer mesesExperienciaMusculacao;
    @JsonProperty("diagnostico_lesao_joelho")
    private String diagnosticoLesaoJoelho;
    private String status;
    @JsonProperty("ausencias_consecutivas")
    private Integer ausenciasConsecutivas;
    @JsonIgnore
    private Boolean listaEspera = false;

}
