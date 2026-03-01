package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.model.RefreshToken;
import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenDurationMs = 7 * 24 * 60* 60 * 1000; // 7 days , 7 * 24 * 60 * 60 * 1000

    public RefreshToken createRefreshToken(StackUser stackUser){
        RefreshToken refreshToken = RefreshToken.builder()
                .stackUser(stackUser)
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plusMillis(refreshTokenDurationMs))
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
