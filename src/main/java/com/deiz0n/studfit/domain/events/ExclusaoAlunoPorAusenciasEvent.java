package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.AlunoDTO;

public record ExclusaoAlunoPorAusenciasEvent(AlunoDTO aluno) {

}
