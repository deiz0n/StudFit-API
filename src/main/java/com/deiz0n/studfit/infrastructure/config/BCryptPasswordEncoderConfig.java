package com.deiz0n.studfit.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderConfig {

    @Bean
    public BCryptPasswordEncoderConfig encoder() {
        return new BCryptPasswordEncoderConfig();
    }

}
