package com.deiz0n.studfit.domain.exceptions.usuario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotExistingException;

public class CargoNotExistentException extends ResourceNotExistingException {

    public CargoNotExistentException(String msg) {
        super(msg);
    }

}
