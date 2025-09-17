package com.deiz0n.studfit.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("StudFit API")
                        .version("1.0")
                        .description("A API do Sistema de Academia foi desenvolvida para apoiar instituições públicas na gestão de alunos e atividades físicas, " +
                                "promovendo maior eficiência e transparência. O sistema automatiza processos como registro de alunos, acompanhamento de frequência e " +
                                "administração de listas de espera, oferecendo uma experiência organizada e confiável para administradores e alunos")
                        .contact(new Contact()
                                .name("Carlos Eduardo Silva")
                                .email("doardo.ns@gmail.com")
                                .url("https://www.linkedin.com/in/carlos-eduardo-ns/"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }

}
