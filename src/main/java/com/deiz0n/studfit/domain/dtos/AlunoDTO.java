package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoDTO {

    private UUID id;
    private Integer colocacao;
    private String nome;
    private Double peso;
    private Integer altura;
    private String email;
    private String telefone;
    private String cirurgias;
    private String patologias;
    @JsonProperty(namespace = "meses_experiencia_musculacao")
    private Integer mesesExperienciaMusculacao;
    @JsonProperty(namespace = "diagnostico_lesao_joelho")
    private String diagnosticoLesaoJoelho;
    private String status;
    @JsonProperty(namespace = "ausencias_consecutivas")
    private Integer ausenciasConsecutivas;
    private Boolean ativado;

}
