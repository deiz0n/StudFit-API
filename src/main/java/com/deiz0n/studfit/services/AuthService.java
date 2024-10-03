package com.deiz0n.studfit.services;

import com.deiz0n.studfit.domain.dtos.AuthDTO;
import com.deiz0n.studfit.domain.dtos.TokenDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private TokenDTO token;
    private AuthenticationManager manager;

    public AuthService(AuthenticationManager manager) {
        this.manager = manager;
    }

    public TokenDTO signIn(AuthDTO auth) {
        var user = new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getSenha());
        var authentication = manager.authenticate(user);

        return token;
    }
}
