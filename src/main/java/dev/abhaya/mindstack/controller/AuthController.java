package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.dto.auth.*;
import dev.abhaya.mindstack.service.auth.AuthService;
import dev.abhaya.mindstack.service.auth.EmailVerificationService;
import dev.abhaya.mindstack.service.auth.RefreshTokenCookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final RefreshTokenCookieService refreshTokenCookieService;
    private final EmailVerificationService emailService;
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @PostMapping("/register")
    public ResponseEntity<String> registerWithEmail(@Valid @RequestBody RegisterEmailRequest emailRequest){
        emailService.sendVerificationEmail(emailRequest.getEmail());
        return ResponseEntity.ok("Verification Email sent");
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token){
        String email = emailService.verifyEmail(token);
        //URI redirectUri = URI.create(frontendUrl );
        //return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
        return ResponseEntity.ok(Map.of("email", email));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest){

        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@Valid @RequestBody LoginUserRequest loginUserRequest,
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

    @DeleteMapping("/delete/account")
    public ResponseEntity<Void> deleteAccount() {
        authService.deleteAccount();
        return ResponseEntity.ok().build();

    }



}
