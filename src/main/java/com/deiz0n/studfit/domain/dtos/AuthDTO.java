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
@Schema(name = "Auth", description = "Dados para autenticação do usuário no sistema")
public class AuthDTO {

    @Email(message = "Email inválido. Tente novamente")
    @NotBlank(message = "O campo email é obrigatório")
    @Schema(description = "E-mail do usuário para login", example = "admin@studfit.com", required = true)
    private String email;

    @NotBlank(message = "O campo senha é obrigatório")
    @Schema(description = "Senha do usuário", example = "minhasenha123", required = true)
    private String senha;

}
