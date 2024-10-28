package com.deiz0n.studfit.domain.exceptions;

public class TurnoNotExistentException extends ResourceNotExistingException {
    public TurnoNotExistentException(String msg) {
        super(msg);
    }
}
