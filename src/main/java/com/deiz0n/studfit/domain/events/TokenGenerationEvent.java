package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import com.deiz0n.studfit.domain.entites.Usuario;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TokenGenerationEvent extends ApplicationEvent {

    private Usuario usuario;

    public TokenGenerationEvent(Object object, Usuario usuario) {
        super(object);
        this.usuario = usuario;
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }

}
