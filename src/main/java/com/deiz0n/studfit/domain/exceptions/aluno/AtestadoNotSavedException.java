package com.deiz0n.studfit.domain.exceptions.aluno;

import com.deiz0n.studfit.domain.exceptions.utils.InternalServerErrorException;

public class AtestadoNotSavedException extends InternalServerErrorException {
    public AtestadoNotSavedException(String msg) {
        super(msg);
    }
}
