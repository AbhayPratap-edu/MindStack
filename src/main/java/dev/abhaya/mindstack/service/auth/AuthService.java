package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.dto.auth.AuthResponse;
import dev.abhaya.mindstack.dto.auth.LoginUserRequest;
import dev.abhaya.mindstack.dto.auth.SignUpRequest;
import dev.abhaya.mindstack.exception.customException.CustomMessageException;
import dev.abhaya.mindstack.exception.customException.UserAlreadyExistsException;
import dev.abhaya.mindstack.model.*;
import dev.abhaya.mindstack.repository.EmailTokenRepository;
import dev.abhaya.mindstack.repository.StackUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final StackUserRepository stackUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserIdentityResolver userIdentityResolver;
    private final TokenService tokenService;
    private final EmailTokenRepository emailTokenRepository;

    public void signUp(SignUpRequest signUpRequest) {

        if(stackUserRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            throw new UserAlreadyExistsException("Cannot signup, user already exits with "+signUpRequest.getEmail());

        String email = signUpRequest.getEmail();

        EmailToken emailToken = emailTokenRepository.findByEmailAndVerifiedTrue(email)
                .orElseThrow(() -> new CustomMessageException("Email not Verified"));

        StackUser newStackUser = StackUser.builder()
                .email(email)
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .authProvider(AuthProvider.LOCAL)
                .role(Role.USER)
                .build();

        stackUserRepository.save(newStackUser);
        emailTokenRepository.delete(emailToken);

    }

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
    public AuthResponse login(LoginUserRequest loginUserRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequest.getEmail(),
                        loginUserRequest.getPassword())
        );

        User userDetails = (User) authentication.getPrincipal();

        StackUser stackUser = userIdentityResolver.resolveFromLocal(userDetails);
        String email = userDetails.getUsername();

        return tokenService.issueTokens(stackUser);

    }

    public AuthResponse rotateRefreshToken(String oldRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(oldRefreshToken);
        StackUser stackUser = refreshToken.getStackUser();

        //Rotation
        refreshTokenService.revokeRefreshToken(oldRefreshToken);

        return tokenService.issueTokens(stackUser);

    }

    public void logout(String refreshToken) {

        if(refreshToken != null) {
            refreshTokenService.revokeRefreshToken(refreshToken);
        }
    }

    public void deleteAccount(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        StackUser user = stackUserRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found"));

        stackUserRepository.deleteById(userId);
    }

}
