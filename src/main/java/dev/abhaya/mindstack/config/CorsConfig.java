package dev.abhaya.mindstack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173",frontendUrl)
                        .allowedMethods("GET","POST","PUT","DELETE")
                        .allowedHeaders("*")//Allow the frontend to send any HTTP headers in the request
                        .allowCredentials(true);//Allow browser to send cookies/credentials in cross-origin requests
            }
        };
    }
}

//.allowedHeaders("*")
//browser frontend will send headers like:
//Authorization: Bearer <JWT>
//Content-Type: application/json
//If not allowed → request gets blocked.

//Without credentials:
//Frontend → Backend (no cookies sent)
//With credentials:
//Frontend → Backend (with cookies attached)
