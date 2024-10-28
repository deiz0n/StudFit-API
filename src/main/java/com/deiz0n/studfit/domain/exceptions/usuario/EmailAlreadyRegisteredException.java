package com.deiz0n.studfit.domain.exceptions.usuario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceAlreadyException;

public class EmailAlreadyRegisteredException extends ResourceAlreadyException {
    public EmailAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
