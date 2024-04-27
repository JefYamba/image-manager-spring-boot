package com.jefy.img.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @Author JefYamba
 * @Email joph.e.f.yamba@gmail.com
 * @Since 27/04/2024
 */

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "JefYamba",
                        email = "joph.e.f.yamba@gmail.com",
                        url = "https://github.com/JefYamba"
                ),
                description = "OpenApi documentation for Image Manager API",
                title = "Image Manager REST API - JefYamba",
                version = "1.1"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}
