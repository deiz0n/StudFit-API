package com.deiz0n.studfit.domain.exceptions.presenca;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceAlreadyException;

public class PresencaAlreadyRegistered extends ResourceAlreadyException {

    public PresencaAlreadyRegistered(String msg) {
        super(msg);
    }

}
