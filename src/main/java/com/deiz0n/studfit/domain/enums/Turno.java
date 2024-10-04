package com.deiz0n.studfit.domain.enums;

public enum Turno {

    MANHA("manha"),
    TARDE("tarde"),
    NOITE("noite");

    private String cargo;

    Turno(String cargo) {
        this.cargo = cargo;
    }
}
