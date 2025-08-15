package com.deiz0n.studfit.infrastructure.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {

    @Bean
    public ModelMapper mapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(
                context -> context.getSource() != null
        );
        return modelMapper;
    }

}
