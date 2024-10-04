package com.deiz0n.studfit.domain.exceptions;

public class UsuarioNotFoundException extends ResourceNotFoundException{

    public UsuarioNotFoundException(String msg) {
        super(msg);
    }
}
