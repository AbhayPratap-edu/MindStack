package dev.abhaya.mindstack.dto.stackuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterStackUserRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6,max = 12)
    private String password;
}
