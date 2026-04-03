package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.model.RefreshToken;
import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60* 60 ; // 7 days ,

    public RefreshToken createRefreshToken(StackUser stackUser){

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        RefreshToken refreshToken = RefreshToken.builder()
                .stackUser(stackUser)
                .token(token)
                .expiresAt(Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token){

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow( () -> new RuntimeException("Invalid refresh token"));

        if(refreshToken.isRevoked()){
            throw new RuntimeException("Refresh token is revoked");
        }
        if(refreshToken.getExpiresAt().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }
        return refreshToken;

    }

    public void revokeRefreshToken(String token){
        refreshTokenRepository.findByToken(token)
                .ifPresent(rt -> {
                    rt.setRevoked(true);
                    refreshTokenRepository.save(rt);
                });
    }

    public void revokeUserAllRefreshTokens(StackUser stackUser){
        refreshTokenRepository.deleteByStackUser(stackUser);
    }
}
