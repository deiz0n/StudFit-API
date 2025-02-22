package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.ResetPasswordDTO;

public record AtualizarSenhaUsuarioEvent(String codigo, ResetPasswordDTO resetPassword) {

}
