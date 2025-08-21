package com.deiz0n.studfit.domain.exceptions.aluno;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotValidException;

public class AtestadoNotValidException extends ResourceNotValidException {
    public AtestadoNotValidException(String message) {
        super(message);
    }
}
