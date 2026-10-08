
package com.example.api_franquicias.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Franquicias")
                        .version("1.0.0")
                        .description("""
                                API REST desarrollada con Spring Boot
                                para administrar franquicias, sucursales
                                y productos.

                                Funcionalidades principales:
                                - Crear y consultar franquicias.
                                - Agregar sucursales a franquicias.
                                - Agregar y eliminar productos.
                                - Actualizar el stock de productos.
                                - Consultar productos con mayor stock.
                                - Renombrar franquicias, sucursales
                                  y productos.

                                Tecnologías:
                                - Java 17
                                - Spring Boot 3.5.6
                                - Spring Data JPA
                                - PostgreSQL
                                - Docker
                                - OpenAPI / Swagger
                                """)
                        .contact(new Contact()
                                .name("Equipo de desarrollo"))
                        .license(new License()
                                .name("Sin licencia definida"))
                );
    }
}
