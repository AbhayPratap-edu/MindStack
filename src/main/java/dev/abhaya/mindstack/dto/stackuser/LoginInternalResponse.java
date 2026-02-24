package dev.abhaya.mindstack.dto.stackuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginInternalResponse {

    private long userID;

    private String accessToken;
    private String refreshToken;


}
