package com.deiz0n.studfit.domain.exceptions.usuario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotFoundException;

public class CodigoDeRecuperacaoNotFoundException extends ResourceNotFoundException {
    public CodigoDeRecuperacaoNotFoundException(String msg) {
        super(msg);
    }
}
