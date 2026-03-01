package dev.abhaya.mindstack.Security.jwt;

import dev.abhaya.mindstack.Security.StackUserDetailsService;
import dev.abhaya.mindstack.model.StackUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final StackUserDetailsService stackUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String token = requestTokenHeader.split("Bearer ")[1];

        Claims claims = jwtService.verifyTokenAndGeClaims(token);

        Long userId = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);
        List<String> permissions = claims.get("permissions", List.class);


        if( userId != null &&  SecurityContextHolder.getContext().getAuthentication() == null){

            StackUser stackUser = stackUserDetailsService.getStackUserById(userId);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            //add role authority
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            // Add permission authorities
            permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(stackUser,null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }

        filterChain.doFilter(request,response);

    }
}
