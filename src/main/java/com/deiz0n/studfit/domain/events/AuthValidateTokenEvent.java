package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuthValidateTokenEvent extends ApplicationEvent {

    private String token;

    public AuthValidateTokenEvent(Object source, String token) {
        super(source);
        this.token = token;
    }
}
