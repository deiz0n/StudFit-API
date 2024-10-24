package com.deiz0n.studfit.domain.entites;

import com.deiz0n.studfit.domain.enums.Cargo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;
    @Column(name = "codigo_recuperacao")
    private String codigoRecuperacao;
    private Cargo cargo;

    @OneToOne
    private Presenca presenca;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (cargo == Cargo.INSTRUTOR)
            return List.of(
                    new SimpleGrantedAuthority("ROLE_INSTRUTOR"),
                    new SimpleGrantedAuthority("ROLE_ESTAGIARIO")
            );
        else if (cargo == Cargo.ADMINISTRADOR)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_ESTAGIARIO"));
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
