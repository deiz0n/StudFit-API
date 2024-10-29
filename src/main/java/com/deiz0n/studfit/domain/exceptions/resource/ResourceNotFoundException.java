package com.deiz0n.studfit.domain.exceptions.resource;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}
