package dev.abhaya.mindstack.dto.auth;

import dev.abhaya.mindstack.model.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;

}
