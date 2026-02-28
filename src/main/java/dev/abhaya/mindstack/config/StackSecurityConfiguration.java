package dev.abhaya.mindstack.config;

import dev.abhaya.mindstack.Security.JwtAuthenticationFilter;
import dev.abhaya.mindstack.Security.oauth2.OAuth2LoginSuccessHandler;
import dev.abhaya.mindstack.Security.oauth2.StackOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                        .requestMatchers(
                                "/auth/signup",
                                "/auth/login",
                                "/auth/refresh",
                                "/login/oauth2/**",
                                "/oauth2/**")
                        .permitAll()
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

    //    @Bean
//    UserDetailsService myInMemoryUserDetailsService(){
//        UserDetails user = User  //for user role
//                .withUsername("<UserName>")  // Replace <UserName> with the actual username
//                .password(passwordEncoder().encode("<UserPassword>"))   // Replace <UserPassword> with the actual password
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User      //for admin role
//                .withUsername("<AdminName>") // Replace <AdminName> with the actual username
//                .password(passwordEncoder().encode("<AdminPassword>"))  // Replace <AdminPassword> with the actual password
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user,admin);
//    }


}