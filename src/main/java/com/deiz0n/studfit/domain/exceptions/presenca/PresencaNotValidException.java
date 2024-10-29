package com.deiz0n.studfit.domain.exceptions.presenca;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotValidException;

public class PresencaNotValidException extends ResourceNotValidException {

    public PresencaNotValidException(String msg) {
        super(msg);
    }

}
