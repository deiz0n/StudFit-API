package com.deiz0n.studfit.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class TokenGenerationEvent extends ApplicationEvent {

    public TokenGenerationEvent(Object email) {
        super(email);
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }
}
