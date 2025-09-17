package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "AtualizarAluno", description = "Dados para atualização de um aluno efetivado")
public class AtualizarAlunoDTO {

    @Schema(description = "Nome completo do aluno", example = "João Santos Silva")
    private String nome;

    @Schema(description = "Peso do aluno em quilogramas", example = "78.5")
    private Double peso;

    @Schema(description = "Altura do aluno em centímetros", example = "180")
    private Integer altura;

    @Email(message = "Email inválido. Tente novamente")
    @Schema(description = "E-mail do aluno", example = "joao.santos@email.com")
    private String email;

    @Schema(description = "Telefone do aluno", example = "11987654321")
    private String telefone;

    @Schema(description = "Histórico de cirurgias do aluno", example = "Apendicectomia em 2019")
    private String cirurgias;

    @Schema(description = "Patologias ou condições médicas do aluno", example = "Diabetes tipo 2")
    private String patologias;

    @JsonProperty("meses_experiencia_musculacao")
    @Schema(description = "Experiência em musculação em meses", example = "18")
    private Integer mesesExperienciaMusculacao;

    @JsonProperty("diagnostico_lesao_joelho")
    @Schema(description = "Diagnóstico de lesão no joelho, se houver", example = "Tendinite patelar")
    private String diagnosticoLesaoJoelho;

    @JsonProperty("consumo_alcool")
    @Schema(description = "Indica se o aluno consome álcool", example = "false")
    private Boolean consumoAlcool = false;

    @JsonProperty("consumo_cigarro")
    @Schema(description = "Indica se o aluno fuma", example = "false")
    private Boolean consumoCigarro = false;

    @JsonProperty("pratica_exercicio_fisico")
    @Schema(description = "Indica se o aluno pratica exercício físico regularmente", example = "true")
    private Boolean praticaExercicioFisico;

    @Schema(description = "Novo horário a ser atribuído ao aluno")
    private HorarioDTO horario;

}
