package dev.abhaya.mindstack.config;

import dev.abhaya.mindstack.Security.JwtAuthenticationFilter;
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
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/auth/signup","/auth/login","/auth/**").permitAll()//give any of you 'get' request endpoint
                        .anyRequest().authenticated())

                .csrf(csrfConfig ->
                        csrfConfig.disable())//disabled for simplicity not for production

                .sessionManagement( sessionConfig ->// Disable JSESSIONID for simplicity,not for production
                        sessionConfig
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build(); //when you add build this throws an exception
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