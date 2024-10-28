package com.deiz0n.studfit.domain.exceptions.aluno;

import com.deiz0n.studfit.domain.exceptions.resource.ResourceNotFoundException;

public class AlunoNotFoundException extends ResourceNotFoundException {

    public AlunoNotFoundException(String msg) {
        super(msg);
    }

}
