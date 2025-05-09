package com.deiz0n.studfit.domain.enums;

public enum Status {

    REGULAR("regular"),
    EM_ALERTA("em_alerta"),
    CADASTRO_PENDENTE("cadastro_pendente");

    private String status;

    Status(String status) {
        this.status = status;
    }

}
