package com.deiz0n.studfit.domain.exceptions;

public class EmailAlreadyRegisteredException extends ResourceAlreadyException{
    public EmailAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
