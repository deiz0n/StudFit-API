package com.deiz0n.studfit.repositories;

import com.deiz0n.studfit.domain.entites.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    UserDetails getByEmail(String email);

}
