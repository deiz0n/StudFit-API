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
    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    private CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(SecurityFilter filter, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) {
        this.filter = filter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(corsConfigurer -> corsConfigurer.configurationSource(getCorsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "api/v1.0/alunos/efetivados",
                                "api/v1.0/alunos/lista-espera",
                                "api/v1.0/alunos/lista-espera/registrar",
                                "api/v1.0/auth/**",
                                "swagger-ui/**",
                                "/v3/**",
                                "/actuator/**"
                        ).permitAll()

                        .requestMatchers("api/v1.0/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "api/v1.0/usuarios").hasRole("ADMINISTRADOR")

                        .requestMatchers("api/v1.0/presencas**").hasRole("ESTAGIARIO")
                        .requestMatchers("api/v1.0/alunos**").hasRole("ESTAGIARIO")

                        .requestMatchers("api/v1.0/horarios**").hasRole("INSTRUTOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();
    }


}
