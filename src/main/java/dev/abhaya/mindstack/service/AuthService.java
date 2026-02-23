package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.Security.JWTService;
import dev.abhaya.mindstack.dto.stackuser.LoginUserRequest;
import dev.abhaya.mindstack.dto.stackuser.SignUpRequest;
import dev.abhaya.mindstack.exception.customException.CustomMessageException;
import dev.abhaya.mindstack.exception.customException.UserAlreadyExistsException;
import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.StackUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StackUserRepository stackUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public void signUp(SignUpRequest signUpRequest) {

        Optional<StackUser> stackUser = stackUserRepository.findByEmail(signUpRequest.getEmail());
        if(stackUser.isPresent())
            throw new UserAlreadyExistsException("Cannot signup, user already exits with "+signUpRequest.getEmail());

        StackUser newStackUser = new StackUser();
        newStackUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        newStackUser.setEmail(signUpRequest.getEmail());

        StackUser savedStackUser = stackUserRepository.save(newStackUser);

    }

    public String logIn(LoginUserRequest loginUserRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequest.getEmail(),
                        loginUserRequest.getPassword()));

        //UsernamePasswordAuthenticationToken holds email and password and
        // acts as an unauthenticated authentication request. It does not authenticate by itself.

        //authenticate(...) receives the unauthenticated Authentication object,
        // delegates to AuthenticationProvider implementations through ProviderManager,
        // and returns a new authenticated Authentication object if credentials are valid.


        //Before authenticate():
        //principal = email
        //credentials = raw password
        //authenticated = false

        //After successful authentication:
        //principal = UserDetails
        //credentials = null
        //authenticated = true

//        System.out.println(authentication);
//        System.out.println(authentication.getPrincipal());


        User userDetails = (User) authentication.getPrincipal();
        String email = userDetails.getUsername();

        StackUser stackUser = stackUserRepository.findByEmail(email)
                .orElseThrow( () -> new CustomMessageException("User Not Found"));

        return jwtService.createToken(stackUser);
    }
}
