package com.deiz0n.studfit.domain.exceptions.horario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceAlreadyException;

public class HorarioAlreadyRegistered extends ResourceAlreadyException {

    public HorarioAlreadyRegistered(String msg) {
        super(msg);
    }
}
