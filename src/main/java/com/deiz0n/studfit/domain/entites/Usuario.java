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
@Entity(name = "tb_usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(length = 150)
    private String nome;
    @Column(unique = true, length = 50)
    private String email;
    private String senha;
    @Column(name = "codigo_recuperacao", length = 6)
    private String codigoRecuperacao;
    @Enumerated(EnumType.STRING)
    private Cargo cargo;

    @OneToMany(mappedBy = "usuario")
    private List<Presenca> presenca;

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
