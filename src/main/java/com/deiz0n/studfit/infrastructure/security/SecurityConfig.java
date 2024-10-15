package com.deiz0n.studfit.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.deiz0n.studfit.infrastructure.config.CorsConfig.getCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private SecurityFilter filter;

    public SecurityConfig(SecurityFilter filter) {
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(corsConfigurer -> corsConfigurer.configurationSource(getCorsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers( "api/v1.0/alunos/efetivados", "api/v1.0/alunos/lista-espera", "api/v1.0/auth/login").permitAll()

                        .requestMatchers("api/v1.0/horarios**").hasRole("INSTRUTOR")

                        .requestMatchers("api/v1.0/alunos**", "api/v1.0/presencas**").hasRole("ESTAGIARIO")

                        .requestMatchers("api/v1.0/usuarios**").hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
