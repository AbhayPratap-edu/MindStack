package dev.abhaya.mindstack.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MindStack API")
                        .version("0.7.0")
                        .description("REST API for consuming mindstack service")
                        .contact(new Contact()
                                .name("Abhay")
                                .email("abhaya.dev@example.com")));
    }
}