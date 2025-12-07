package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Schema(name = "Aluno", description = "Dados completos de um aluno para efetivação")
public class AlunoDTO {

    @Schema(description = "ID único do aluno", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Schema(description = "Nome completo do aluno", example = "João Santos", required = true)
    private String nome;

    @Schema(description = "Peso do aluno em quilogramas", example = "75.5")
    private Double peso;

    @Schema(description = "Altura do aluno em centímetros", example = "175")
    private Integer altura;

    @Email(message = "Email inválido. Tente novamente")
    @Schema(description = "E-mail do aluno", example = "joao.santos@email.com", required = true)
    private String email;

    @NotBlank(message = "O campo telefone é obrigatório")
    @Schema(description = "Telefone do aluno", example = "11987654321", required = true)
    private String telefone;

    @Schema(description = "Histórico de cirurgias do aluno", example = "Cirurgia no joelho em 2020")
    private String cirurgias;

    @Schema(description = "Patologias ou condições médicas do aluno", example = "Hipertensão")
    private String patologias;

    @JsonProperty("meses_experiencia_musculacao")
    @Schema(description = "Experiência em musculação em meses", example = "12")
    private Integer mesesExperienciaMusculacao;

    @JsonProperty("diagnostico_lesao_joelho")
    @Schema(description = "Diagnóstico de lesão no joelho, se houver", example = "Condromalácia patelar")
    private String diagnosticoLesaoJoelho;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Status atual do aluno no sistema", example = "ATIVO", allowableValues = {"ATIVO", "INATIVO", "SUSPENSO"}, accessMode = Schema.AccessMode.READ_ONLY)
    private Status status;

    @JsonProperty(value = "ausencias_consecutivas", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Número de ausências consecutivas", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer ausenciasConsecutivas;

    @JsonIgnore
    @Schema(hidden = true)
    private Boolean listaEspera = false;

    @JsonProperty("consumo_alcool")
    @Schema(description = "Indica se o aluno consome álcool", example = "false")
    private Boolean consumoAlcool = false;

    @JsonProperty("consumo_cigarro")
    @Schema(description = "Indica se o aluno fuma", example = "false")
    private Boolean consumoCigarro = false;

    @JsonProperty("pratica_exercicio_fisico")
    @Schema(description = "Indica se o aluno pratica exercício físico regularmente", example = "true")
    private Boolean praticaExercicioFisico;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Array de turnos preferenciais", example = "[\"MANHA\", \"TARDE\"]", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String[] turnosPreferenciais;

    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora da última atualização", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;

    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data/hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;

    @Schema(description = "Horário atribuído ao aluno")
    private HorarioDTO horario;

    @Schema(description = "URL no S3 do atestado", accessMode = Schema.AccessMode.READ_ONLY)
    private String atestado;
}
