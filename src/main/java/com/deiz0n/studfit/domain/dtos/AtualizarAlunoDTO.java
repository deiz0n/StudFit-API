package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtualizarAlunoDTO {

    private String nome;
    private Double peso;
    private Integer altura;
    @Email(message = "Email inválido. Tente novamente")
    private String email;
    private String telefone;
    private String cirurgias;
    private String patologias;
    @JsonProperty("meses_experiencia_musculacao")
    private Integer mesesExperienciaMusculacao;
    @JsonProperty("diagnostico_lesao_joelho")
    private String diagnosticoLesaoJoelho;
    @JsonProperty("consumo_alcool")
    private Boolean consumoAlcool = false;
    @JsonProperty("consumo_cigarro")
    private Boolean consumoCigarro = false;
    @JsonProperty("pratica_exercicio_fisico")
    private Boolean praticaExercicioFisico;

    private HorarioDTO horario;

}
