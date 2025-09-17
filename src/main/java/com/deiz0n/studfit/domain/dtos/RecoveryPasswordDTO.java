package com.deiz0n.studfit.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "RecoveryPassword", description = "Dados para solicitação de recuperação de senha")
public class RecoveryPasswordDTO {

    @Email(message = "Email inválido. Tente novamente")
    @NotBlank(message = "O campo email é obrigatório")
    @Schema(description = "E-mail do usuário para recuperação de senha", example = "usuario@studfit.com", required = true)
    private String email;

}
