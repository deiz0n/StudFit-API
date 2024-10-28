package com.deiz0n.studfit.domain.exceptions.usuario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceAlreadyException;

public class TelefoneAlreadyRegistered extends ResourceAlreadyException {
    public TelefoneAlreadyRegistered(String msg) {
        super(msg);
    }
}
