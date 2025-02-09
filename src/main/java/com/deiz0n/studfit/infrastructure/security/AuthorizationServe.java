package com.deiz0n.studfit.infrastructure.security;

import com.deiz0n.studfit.infrastructure.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServe implements UserDetailsService {

    private UsuarioRepository repository;

    public AuthorizationServe(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.buscarUserDetail(username);
    }
}
