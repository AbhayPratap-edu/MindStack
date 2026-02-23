package dev.abhaya.mindstack.controller;

import dev.abhaya.mindstack.dto.stackuser.LoginUserRequest;
import dev.abhaya.mindstack.dto.stackuser.RegisterStackUserRequest;
import dev.abhaya.mindstack.dto.stackuser.RegisterStackUserResponse;
import dev.abhaya.mindstack.dto.stackuser.SignUpRequest;
import dev.abhaya.mindstack.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest){
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserRequest loginUserRequest) {
        String token = authService.logIn(loginUserRequest);

        //Add Arguments to loginMethod(HttpServletRequest request, HttpServletResponse response)
        //Cookie cookie = new Cookie("token", token);
        //cookie.setHttpOnly(true); // it makes sure that this cookie cannot be accessed by any other,
                                    // it can only be fund with the help of your Http methods
                                    // Prevents JavaScript access to the cookie
        //response.addCookie(cookie); // Http only cookies can be passed from backend to frontend only

        return ResponseEntity.ok(token);
    }

}
