package com.deiz0n.studfit.domain.dtos;

import com.deiz0n.studfit.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunoEfetivadoDTO {

    private UUID id;
    private String nome;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HorarioDTO horario;
    private Status status;

}
