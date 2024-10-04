package com.deiz0n.studfit.domain.events;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TokenGeneratedEvent extends ApplicationEvent {

    private String token;

    public TokenGeneratedEvent(Object source, String token) {
        super(source);
        this.token = token;
    }

}
