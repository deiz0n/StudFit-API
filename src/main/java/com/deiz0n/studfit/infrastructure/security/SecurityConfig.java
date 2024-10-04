package com.deiz0n.studfit.infrastructure.security;

import com.deiz0n.studfit.infrastructure.security.exceptions.CustomAccessDeniedHandler;
import com.deiz0n.studfit.infrastructure.security.exceptions.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CustomAccessDeniedHandler accessDeniedHandler;
    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    private SecurityFilter filter;

    public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint, SecurityFilter filter) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("api/v1.0/usuarios**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "api/v1.0/alunos/lista-espera").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/v1.0/alunos/lista-espera/register").hasRole("ESTAGIARIO")
                        .requestMatchers(HttpMethod.DELETE, "api/v1.0/alunos/lista-espera/delete/{id}").hasRole("ESTAGIARIO")

                        .requestMatchers(HttpMethod.GET, "api/v1.0/alunos/efetivados").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "api/v1.0/alunos/efetivados/efetivar").hasRole("ESTAGIARIO")
                        .requestMatchers(HttpMethod.DELETE, "api/v1.0/alunos/efetivados/delete/{id}").hasRole("ESTAGIARIO")
                        .requestMatchers(HttpMethod.PATCH, "api/v1.0/alunos/efetivados/update/{id}").hasRole("ESTAGIARIO")

                        .requestMatchers(HttpMethod.POST, "api/v1.0/auth/login").permitAll()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();
    }

}
