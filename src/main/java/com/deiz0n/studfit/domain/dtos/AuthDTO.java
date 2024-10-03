package com.deiz0n.studfit.domain.dtos;

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
public class AuthDTO {

    @Email(message = "Email inválido. Tente novamente")
    @NotBlank(message = "O campo email é obrigatório")
    private String email;
    @NotBlank(message = "O campo senha é obrigatório")
    private String senha;

}
