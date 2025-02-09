package com.deiz0n.studfit.infrastructure.repositories;

import com.deiz0n.studfit.domain.entites.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    @Query("FROM tb_usuario u WHERE u.email = :email")
    UserDetails buscarUserDetail(String email);

    @Query("FROM tb_usuario u WHERE u.email = :email")
    Optional<Usuario> buscarPorEmail(String email);

    @Query("FROM tb_usuario u WHERE u.codigoRecuperacao = :codigo")
    Optional<Usuario> buscarPorCodigoRecuperacao(String codigo);

}
