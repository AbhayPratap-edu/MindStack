package dev.abhaya.mindstack.service.auth;

import dev.abhaya.mindstack.model.AuthProvider;
import dev.abhaya.mindstack.model.Role;
import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.StackUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserIdentityResolverImpl implements UserIdentityResolver {

    private final StackUserRepository stackUserRepository;

    @Override
    public StackUser resolveFromLocal(UserDetails userDetails) {

        String email = userDetails.getUsername();

        StackUser stackUser = stackUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if(stackUser.getPassword()==null){
            throw new RuntimeException("Password Login Not Allowed For OAuth Users");
        }

        return stackUser;
    }

    @Override
    public StackUser resolveFromOAuth(OAuth2User oAuth2User) {

        String email = oAuth2User.getAttribute("email");
        String provideId = oAuth2User.getAttribute("sub");


        if(email==null){
            throw new RuntimeException("Email not provided by OAuth provider");
        }

        StackUser existingUser = stackUserRepository.findByEmail(email).orElse(null);

        if(existingUser != null){
            return existingUser;
        }

        //create new OAuth User
        StackUser newStackUser = new StackUser();
        newStackUser.setEmail(email);
        newStackUser.setPassword(null);
        newStackUser.setAuthProvider(AuthProvider.GOOGLE);
        newStackUser.setProviderId(provideId);
        newStackUser.setRole(Role.USER);
        return stackUserRepository.save(newStackUser);
    }
}
