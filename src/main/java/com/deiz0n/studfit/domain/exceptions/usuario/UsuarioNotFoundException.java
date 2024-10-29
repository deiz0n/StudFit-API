package com.deiz0n.studfit.domain.exceptions.usuario;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotFoundException;

public class UsuarioNotFoundException extends ResourceNotFoundException {

    public UsuarioNotFoundException(String msg) {
        super(msg);
    }
}
