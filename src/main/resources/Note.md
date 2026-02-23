Client Request
    ↓
Security Filter Chain
    ↓
    (Authentication happens here)
    ↓
Controller
    ↓
Service

| Layer                 | Component                  |
| --------------------- | -------------------------- |
| Security Filter Layer | `AuthenticationEntryPoint` |
| Controller Layer      | `@RestControllerAdvice`    |

| Feature       | AuthenticationEntryPoint                       | RestControllerAdvice        |
| ------------- | ---------------------------------------------- | --------------------------- |
| Layer         | Security Filter Chain                          | Controller Layer            |
| Trigger       | Authentication failure                         | Application exceptions      |
| JWT errors    | YES (in filter)                                | Usually NO                  |
| Access denied | Sometimes (403 handled by AccessDeniedHandler) | Yes if thrown in controller |
| Returns       | Writes directly to HttpServletResponse         | Returns ResponseEntity      |
| Configured in | SecurityConfig                                 | Component scanning          |

| Situation                         | Handler                  |
| --------------------------------- | ------------------------ |
| Not authenticated (401)           | AuthenticationEntryPoint |
| Authenticated but forbidden (403) | AccessDeniedHandler      |

### Project Structre 

```text
com.yourapp
│
├── config
│     ├── SecurityConfig.java
│
├── security
│     ├── JwtAuthenticationFilter.java
│     ├── JwtAuthenticationEntryPoint.java
│     ├── CustomAccessDeniedHandler.java
│
├── controller
│     ├── AuthController.java
│     ├── ContentController.java
│
├── service
│
├── repository
│
└── exception
      ├── GlobalExceptionHandler.java  
      ├── ApiError.java
```

### JwtAuthenticationEntryPoint (401 handler)

```bash

package com.yourapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write("""
            {
              "error": "Unauthorized",
              "message": "Authentication required or token invalid"
            }
        """);
    }
}

```
### CustomAccessDeniedHandler (403 handler)

```bash

package com.yourapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        response.getWriter().write("""
            {
              "error": "Forbidden",
              "message": "You do not have permission to access this resource"
            }
        """);
    }
}

```

### SecurityConfig

```bash

package com.yourapp.config;

import com.yourapp.security.JwtAuthenticationEntryPoint;
import com.yourapp.security.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            );

        return http.build();
    }
}

```

### RestControllerAdvice GlobalException Handler for RestApi

```bash

package dev.abhaya.mindstack.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //AuthorizationException
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        ApiError apiError = new ApiError(accessDeniedException.getLocalizedMessage(),
                HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    //AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException authenticationException){
        ApiError apiError = new ApiError(authenticationException.getLocalizedMessage(),HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

    //JWTException
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException jwtException){
        ApiError apiError = new ApiError(jwtException.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    //@ExceptionHandler(ControllerThrowException.class)

}

```

### RestController Example

```bash
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/{id}")
    public ContentResponse getContent(@PathVariable Long id) {

        Content content = contentService.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Content not found with id: " + id));

        return ContentResponse.from(content);
    }
}
```