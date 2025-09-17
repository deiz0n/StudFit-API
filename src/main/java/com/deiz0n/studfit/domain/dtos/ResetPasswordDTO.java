package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "ResetPassword", description = "Dados para redefinir a senha do usuário")
public class ResetPasswordDTO {

    @NotBlank(message = "O campo senha é obrigatório")
    @Schema(description = "Nova senha do usuário", example = "novaSenha123", required = true)
    private String senha;

    @NotBlank(message = "O campo senha é obrigatório")
    @JsonProperty("confirmar_senha")
    @Schema(description = "Confirmação da nova senha (deve ser idêntica à senha)", example = "novaSenha123", required = true)
    private String confirmarSenha;

}
