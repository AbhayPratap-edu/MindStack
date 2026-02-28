package dev.abhaya.mindstack.Security.oauth2;

import dev.abhaya.mindstack.model.AuthProvider;
import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.StackUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

//Loads user attributes from Google
//Returns OAuth2User

@Component
public class StackOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        if (email == null ) {
            throw new OAuth2AuthenticationException("Email not provided by OAuth2 provider");
        }

        return oAuth2User;
    }
}

