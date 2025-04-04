package com.deiz0n.studfit.domain.exceptions.usuario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceAlreadyException;

public class TelefoneAlreadyRegisteredException extends ResourceAlreadyException {
    public TelefoneAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
