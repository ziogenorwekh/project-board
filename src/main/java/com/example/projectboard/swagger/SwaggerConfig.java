package com.example.projectboard.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "게시판 API",
                description = "게시판 API 명세서",
                version = "1.0",
                contact = @Contact(
                        name = "LSEK"
                )
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customTestOpenApi() {

        return GroupedOpenApi
                .builder()
                .group("ALL API")
                .addOpenApiCustomizer(buildSecurityOpenApiCustomizer()).build();
    }

    public OpenApiCustomizer buildSecurityOpenApiCustomizer() {
        return OpenApi -> OpenApi.addSecurityItem(new SecurityRequirement().addList("jwt token"))
                .getComponents().addSecuritySchemes("jwt token", new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer"));
    }
}
