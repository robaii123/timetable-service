package com.example.timetable_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI timetableOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Timetable Planning API")
                        .description("API for scheduling sessions using OptaPlanner")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Planner Service")
                                .url("https://timetable-web-cbbx.onrender.com")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Swagger UI")
                        .url("/swagger-ui.html"));
    }
}
