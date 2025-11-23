package com.learning.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "learning_spring",
                        email = "learningspring@email.com",
                        url = "https://localhost:8081/"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "Open Api Specification - spring security",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://localhost:8001"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "local env",
                        url = "http://localhost:8001"
                ),
                @Server(
                        description = "prod env",
                        url = "https://produrl"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "enter JWT authentication token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER

)
public class OpenApiConfig {
}
