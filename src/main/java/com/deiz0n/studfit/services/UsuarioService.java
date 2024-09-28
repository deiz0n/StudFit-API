package com.deiz0n.studfit.services;

import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }
}
