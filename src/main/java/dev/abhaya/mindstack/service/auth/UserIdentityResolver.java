package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.model.StackUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserIdentityResolver {
    StackUser resolveFromLocal(UserDetails userDetails);
    StackUser resolveFromOAuth(OAuth2User oAuth2User);
}
//It takes:
//          UserDetails (local login)
//          OAuth2User (Google login)
//And returns:StackUser