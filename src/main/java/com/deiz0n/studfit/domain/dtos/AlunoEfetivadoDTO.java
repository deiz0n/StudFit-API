package com.deiz0n.studfit.domain.dtos;

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
    private HorarioDTO horario;

}
