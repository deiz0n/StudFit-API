package com.deiz0n.studfit.domain.events;

public record EnviarEmailRecuperacaoSenhaEvent(String[] destinatario, String codigo) {

}
