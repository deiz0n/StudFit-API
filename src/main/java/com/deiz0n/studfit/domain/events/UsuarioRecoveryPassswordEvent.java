package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.RecoveryPasswordDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UsuarioRecoveryPassswordEvent extends ApplicationEvent {

    private String email;

    public UsuarioRecoveryPassswordEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
