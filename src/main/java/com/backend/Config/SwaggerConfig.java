package com.backend.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final String TITLE = "Car Wash API";
    private static final String DESCRIPTION = "Java Backend API Documentation";
    private static final String VERSION = "v1.0.0";

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${server.port}") String port,
            @Value("${server.address:localhost}") String address,
            @Value("${server.servlet.context-path}") String contextPath
    ) {

        String baseUrl = "http://" + address + ":" + port + contextPath;

        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION))

                .servers(List.of(
                        new Server()
                                .url(baseUrl)
                                .description("Current Environment")
                ))

                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )

                .security(List.of(
                        new SecurityRequirement().addList(securitySchemeName)
                ));
    }
}