package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.enums.Cargo;
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
public class UsuarioDTO {

    private UUID id;
    @NotBlank(message = "O campo nome é  obrigatório")
    private String nome;
    @Email(message = "Email inválido. Tente novamente")
    private String email;
    @NotBlank(message = "O campo cargo é  obrigatório")
    private Cargo cargo;

}
