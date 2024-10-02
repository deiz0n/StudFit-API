package com.deiz0n.studfit.domain.exceptions;

import org.springframework.http.converter.HttpMessageNotReadableException;

public class CargoNotExistentException extends HttpMessageNotReadableException {

    public CargoNotExistentException(String msg) {
        super(msg);
    }

}
