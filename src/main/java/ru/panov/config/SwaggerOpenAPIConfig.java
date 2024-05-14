package ru.panov.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger OpenAPI.
 */
@Configuration
@ComponentScan(basePackages = {"org.springdoc"})
@OpenAPIDefinition(
        info = @Info(
                title = "Training Diary Api",
                description = """
                        Training Diary API
                        <p><b>Тестовые данные:</b><br>
                        - admin: admin / admin<br>
                        - user: user1 / user1<br>
                        """,
                version = "1.0.0",
                contact = @Contact(
                        name = "Aleksey Panov",
                        email = "evil199315@yandex.ru"
                )
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerOpenAPIConfig {
}