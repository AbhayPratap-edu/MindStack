package dev.abhaya.mindstack.dto.stackuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterStackUserResponse {

    private long userID;
    private String email;

}
