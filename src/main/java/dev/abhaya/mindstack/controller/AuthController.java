package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.dto.auth.*;
import dev.abhaya.mindstack.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest){
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@RequestBody LoginUserRequest loginUserRequest,
                                                       HttpServletResponse httpServletResponse) {
        AuthResponse loginResponse = authService.login(loginUserRequest);

        Cookie cookie = new Cookie("refresh_token", loginResponse.getRefreshToken());
        cookie.setHttpOnly(true);//JavaScript cannot access this cookie. Only browser automatically sends it in requests
        cookie.setSecure(true);//sent over only HTTPS. not over HTTP
        cookie.setPath("/auth/refresh");//Browser Send this cookie only when request path starts with /auth/refresh.
        cookie.setMaxAge(7 * 24 * 60 * 60);//Cookie persists for 7 days,should match refresh token validation time
        cookie.setAttribute("SameSite", "Strict");//Cookie is sent ONLY when request originates from same site.
        httpServletResponse.addCookie(cookie);//attaches it to HTTP response header

        return ResponseEntity.ok(new LoginApiResponse
                        (loginResponse.getUserId(),
                        loginResponse.getAccessToken())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginApiResponse> refreshToken(HttpServletRequest httpServletRequest,
                                                              HttpServletResponse httpServletResponse) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if(refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthResponse loginResponse = authService.rotateRefreshToken(refreshToken);
        Cookie cookie = new Cookie("refresh_token", loginResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "Strict");

        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new LoginApiResponse(
                        loginResponse.getUserId(),
                        loginResponse.getAccessToken())
        );
    }

}
