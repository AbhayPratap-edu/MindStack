package dev.abhaya.mindstack.config;

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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class StackSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                            StackOAuth2UserService stackOAuth2UserService,
                                            OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) throws Exception{
        httpSecurity
                .authorizeHttpRequests( auth -> auth

                        //Spring stops at the first matching rule

                        //public
                        .requestMatchers(
                                "/auth/signup",
                                "/auth/login",
                                "/auth/refresh",
                                "/login/oauth2/**",
                                "/oauth2/**")
                        .permitAll()

                        // Chapter nested endpoints FIRST
                        .requestMatchers(HttpMethod.GET, "/notebooks/*/chapters")
                        .hasAuthority("CHAPTER_VIEW")

                        .requestMatchers(HttpMethod.POST, "/notebooks/*/chapters")
                        .hasAuthority("CHAPTER_CREATE")

                        .requestMatchers(HttpMethod.GET, "/chapters/**")
                        .hasAuthority("CHAPTER_VIEW")

                        .requestMatchers(HttpMethod.PATCH, "/chapters/**")
                        .hasAuthority("CHAPTER_UPDATE")

                        .requestMatchers(HttpMethod.DELETE, "/chapters/**")
                        .hasAuthority("CHAPTER_DELETE")

                        // Notebook endpoints
                        .requestMatchers(HttpMethod.GET, "/notebooks/**")
                        .hasAuthority("NOTEBOOK_VIEW")

                        .requestMatchers(HttpMethod.POST, "/notebooks/**")
                        .hasAuthority("NOTEBOOK_CREATE")

                        .requestMatchers(HttpMethod.PUT, "/notebooks/**")
                        .hasAuthority("NOTEBOOK_UPDATE")

                        .requestMatchers(HttpMethod.DELETE, "/notebooks/**")
                        .hasAuthority("NOTEBOOK_DELETE")


                        // /notebooks/** → matches /notebooks and /notebooks/{id}
                        // /notebooks/*/chapters → matches /notebooks/1/chapters,
                        // /chapters/** → matches /chapters/{id}

                        //Admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated())

                .exceptionHandling(ex ->
                        ex
                                .authenticationEntryPoint(
                                        (request, response, authException)
                                                -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                                )
                )

                .csrf(csrfConfig ->
                        csrfConfig.disable())//disabled for simplicity not for production

                .sessionManagement( sessionConfig ->
                        sessionConfig
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))


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