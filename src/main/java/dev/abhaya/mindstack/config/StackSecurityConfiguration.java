package dev.abhaya.mindstack.config;

import dev.abhaya.mindstack.Security.jwt.CustomAccessDeniedHandler;
import dev.abhaya.mindstack.Security.jwt.JwtAuthenticationEntryPoint;
import dev.abhaya.mindstack.Security.jwt.JwtAuthenticationFilter;
import dev.abhaya.mindstack.Security.oauth2.OAuth2LoginSuccessHandler;
import dev.abhaya.mindstack.Security.oauth2.StackOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class StackSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                            StackOAuth2UserService stackOAuth2UserService,
                                            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) throws Exception{
        httpSecurity
                .cors(cors -> {} )
                .csrf(csrfConfig ->
                        csrfConfig.disable())//disabled for simplicity not for production

                .sessionManagement( sessionConfig ->
                        sessionConfig
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .authorizeHttpRequests( auth -> auth

                        //Spring stops at the first matching rule

                        //public
                        .requestMatchers(
                                "/",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "auth/register",
                                "auth/verify",
                                "/auth/signup",
                                "/auth/login",
                                "/auth/refresh",
                                "/login/oauth2/**",
                                "/oauth2/**")
                        .permitAll()
                        .anyRequest().authenticated())

                .exceptionHandling(ex ->
                        ex
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )





                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login( oauth2LoginConfig ->
                        oauth2LoginConfig
                                .userInfoEndpoint( userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(stackOAuth2UserService))
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler((request, response, exception) ->
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage())
                                )
                )
        ;

        return httpSecurity.build(); //when you add build this throws an exception
    }


    // Password Encoding
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}