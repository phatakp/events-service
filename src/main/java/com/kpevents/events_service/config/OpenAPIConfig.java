package com.kpevents.events_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    private final AppProperties appProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl(appProperties.getUrl());
        server.setDescription("OpenAPI Documentation for KP Events API");

        Contact contact = new Contact();
        contact.setName("Praveen Phatak");
        contact.setEmail("praveenphatak@gmail.com");

        Info info = new Info()
                .title("KP Events API")
                .description("KP Events API")
                .version("1.0")
                .contact(contact);
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
