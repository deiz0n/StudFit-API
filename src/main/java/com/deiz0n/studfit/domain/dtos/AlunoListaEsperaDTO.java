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
public class AlunoListaEsperaDTO {

    private UUID id;
    private String nome;
    private String email;
    private Integer colocacao;
    private Boolean ativado = false;

}