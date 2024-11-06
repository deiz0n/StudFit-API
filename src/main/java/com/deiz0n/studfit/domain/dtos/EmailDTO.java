package com.deiz0n.studfit.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDTO {

    @Email(message = "Email inválido. Tente novamente")
    @NotBlank(message = "O campo email é obrigatório")
    private String[] destinatario;
    private String titulo;
    private String conteudo;
    @Singular("variavel")
    private Map<String, Object> variaveis;

}
