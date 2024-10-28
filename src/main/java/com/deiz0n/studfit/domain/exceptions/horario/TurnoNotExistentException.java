package com.deiz0n.studfit.domain.exceptions.horario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotExistingException;

public class TurnoNotExistentException extends ResourceNotExistingException {
    public TurnoNotExistentException(String msg) {
        super(msg);
    }
}