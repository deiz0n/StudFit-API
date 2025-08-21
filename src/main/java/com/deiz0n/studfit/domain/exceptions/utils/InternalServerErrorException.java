package com.deiz0n.studfit.domain.exceptions.utils;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String msg) {
        super(msg);
    }
}
