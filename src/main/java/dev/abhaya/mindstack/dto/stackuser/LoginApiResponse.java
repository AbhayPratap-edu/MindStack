package dev.abhaya.mindstack.dto.stackuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginApiResponse {

    private long userID;
    private String accessToken;
}
