package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class SentAlunoDeletedToUsuariosEvent extends ApplicationEvent {

    private String nomeAluno;
    private List<UsuarioDTO> usuarios;

    public SentAlunoDeletedToUsuariosEvent(Object source, List<UsuarioDTO> usuarios, String nomeAluno) {
        super(source);
        this.usuarios = usuarios;
        this.nomeAluno = nomeAluno;
    }
}
