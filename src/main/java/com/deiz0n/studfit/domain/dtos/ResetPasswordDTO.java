package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordDTO {

    @NotBlank(message = "O campo senha é obrigatório")
    private String senha;
    @NotBlank(message = "O campo senha é obrigatório")
    @JsonProperty("confirmar_senha")
    private String confirmarSenha;

}
