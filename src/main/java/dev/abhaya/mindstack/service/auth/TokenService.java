package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.Security.jwt.JWTService;
import dev.abhaya.mindstack.dto.auth.AuthResponse;
import dev.abhaya.mindstack.model.RefreshToken;
import dev.abhaya.mindstack.model.StackUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse issueTokens(StackUser stackUser) {

        String accessToken = jwtService.createAccessToken(stackUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(stackUser);

        return new AuthResponse(stackUser
                .getUserId(),
                accessToken,
                refreshToken.getToken());
    }
}
