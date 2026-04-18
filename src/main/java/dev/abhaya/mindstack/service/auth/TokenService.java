package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.Security.jwt.JWTService;
import dev.abhaya.mindstack.dto.auth.AuthResponse;
import dev.abhaya.mindstack.model.RefreshToken;
import dev.abhaya.mindstack.model.StackUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenCookieService refreshTokenCookieService;

    public AuthResponse issueAccessTokens(StackUser stackUser) {

        String accessToken = jwtService.createAccessToken(stackUser);

        return new AuthResponse(stackUser
                .getUserId(),
                accessToken);
    }

    //this will create refresh token & set in cookie
    public AuthResponse issueRefreshToken(StackUser stackUser, HttpServletResponse httpServletResponse) {

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(stackUser);
        refreshTokenCookieService.addRefreshTokenCookie(httpServletResponse,refreshToken.getToken());


        return new AuthResponse(stackUser
                .getUserId(),
                null);
    }
}
