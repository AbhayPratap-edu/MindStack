package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.dto.auth.*;
import dev.abhaya.mindstack.service.AuthService;
import dev.abhaya.mindstack.service.RefreshTokenCookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final RefreshTokenCookieService refreshTokenCookieService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest){
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@RequestBody LoginUserRequest loginUserRequest,
                                                       HttpServletResponse httpServletResponse) {

        AuthResponse loginResponse = authService.login(loginUserRequest);
        refreshTokenCookieService.addRefreshTokenCookie(httpServletResponse,loginResponse.getRefreshToken());

        return ResponseEntity.ok(new LoginApiResponse
                        (loginResponse.getUserId(),
                        loginResponse.getAccessToken())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginApiResponse> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {


        if(refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthResponse loginResponse = authService.rotateRefreshToken(refreshToken);
        refreshTokenCookieService.addRefreshTokenCookie(httpServletResponse,loginResponse.getRefreshToken());

        return ResponseEntity.ok(new LoginApiResponse(
                        loginResponse.getUserId(),
                        loginResponse.getAccessToken())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse httpServletResponse) {

        authService.logout(refreshToken);
        refreshTokenCookieService.clearRefreshTokenCookie(httpServletResponse);

        return ResponseEntity.ok().build();

    }


}
