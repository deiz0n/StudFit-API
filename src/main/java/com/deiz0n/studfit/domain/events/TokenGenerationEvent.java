package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.UsuarioDTO;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@Builder
public class TokenGenerationEvent extends ApplicationEvent {

    private UsuarioDTO usuario;

    public TokenGenerationEvent(Object object, UsuarioDTO usuario) {
        super(object);
        this.usuario = usuario;
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }
}
