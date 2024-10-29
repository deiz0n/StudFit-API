package com.deiz0n.studfit.domain.events;

import com.deiz0n.studfit.domain.dtos.ResetPasswordDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UsuarioResetPasswordEvent extends ApplicationEvent {

    private String codigo;
    private ResetPasswordDTO resetPassword;

    public UsuarioResetPasswordEvent(Object source, String codigo, ResetPasswordDTO resetPassword) {
        super(source);
        this.codigo = codigo;
        this.resetPassword = resetPassword;
    }
}
