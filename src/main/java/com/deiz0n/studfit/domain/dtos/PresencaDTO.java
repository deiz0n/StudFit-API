package com.deiz0n.studfit.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PresencaDTO {

    private UUID id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;
    private Boolean presente;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AlunoDTO aluno;
    private UsuarioDTO usuario;
}
