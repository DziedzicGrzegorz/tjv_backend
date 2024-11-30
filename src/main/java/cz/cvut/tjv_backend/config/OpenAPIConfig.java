package cz.cvut.tjv_backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${application.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI customOpenAPI() {

        Server prodServer = new Server()
                .url(prodUrl)
                .description("Server URL in Production environment");

        Contact contact = new Contact()
                .email("dziedzgrz@fit.cvut.cz")
                .name("Grzegorz Dziedzic")
                .url("https://github.com/DziedzicGrzegorz");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("API for TJV course REST API")
                .version("1.0")
                .description("This API exposes endpoints to manage tutorials.")
                .termsOfService("https://github.com/DziedzicGrzegorz")
                .contact(contact)
                .license(mitLicense);

        ExternalDocumentation externalDocs = new ExternalDocumentation()
                .description("Additional Documentation")
                .url("https://github.com/DziedzicGrzegorz");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Use the Bearer token for authentication.");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .info(info)
                .externalDocs(externalDocs)
                .servers(List.of(prodServer))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("Bearer Authentication", securityScheme);
    }
}