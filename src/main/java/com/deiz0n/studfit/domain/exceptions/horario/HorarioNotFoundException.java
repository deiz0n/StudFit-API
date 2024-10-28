package com.deiz0n.studfit.domain.exceptions.horario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotFoundException;

public class HorarioNotFoundException extends ResourceNotFoundException {

    public HorarioNotFoundException(String msg) {
        super(msg);
    }
}
