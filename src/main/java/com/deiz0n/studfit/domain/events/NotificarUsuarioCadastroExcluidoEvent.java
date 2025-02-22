package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;

import java.util.List;

public record NotificarUsuarioCadastroExcluidoEvent(List<UsuarioDTO> usuarios, String nomeAluno) {

}
