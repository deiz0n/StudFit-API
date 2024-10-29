package com.deiz0n.studfit.domain.exceptions.horario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotValidException;

public class HorarioNotValidException extends ResourceNotValidException {

    public HorarioNotValidException(String msg) {
        super(msg);
    }
}
