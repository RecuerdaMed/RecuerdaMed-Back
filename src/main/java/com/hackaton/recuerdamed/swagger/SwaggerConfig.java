package com.hackaton.recuerdamed.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RecuerdaMed API")
                        .version("0.0.1")
                        .description("API REST to manage medication and medication intake reminders")
                        .contact(new Contact()
                                .name("RecuerdaMed Team")
                                .email("support@recuerdamed.com")
                                .url("https://recuerdamed.com"))
                        .license(new License()
                                .name("Apache License 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.txt")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("https://api.recuerdamed.com")
                                .description("Production server")
                ));
    }
}
