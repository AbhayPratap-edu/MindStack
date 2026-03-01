package dev.abhaya.mindstack.Security.oauth2;

import dev.abhaya.mindstack.dto.auth.AuthResponse;
import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.service.auth.TokenService;
import dev.abhaya.mindstack.service.auth.UserIdentityResolverImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//After OAuth2 authentication succeeds:
//      Spring does NOT automatically generate your JWT.
//      It only sets authentication in SecurityContext.
//You need:
//      Extract OAuth2User
//      Convert to UserEntity
//      Issue JWT + Refresh token
//      Write response
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserIdentityResolverImpl userIdentityResolver;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User  oAuth2User = (OAuth2User) authentication.getPrincipal();

        StackUser stackUser = userIdentityResolver.resolveFromOAuth(oAuth2User);

        AuthResponse authResponse = tokenService.issueTokens(stackUser);
        Cookie refreshTokenCookie = new Cookie("refresh_token", authResponse.getRefreshToken());

        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/auth/refresh");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshTokenCookie.setSecure(true);   // production
        refreshTokenCookie.setAttribute("SameSite", "Strict");

        response.addCookie(refreshTokenCookie);

        response.setContentType("application/json");
        response.getWriter().write("{ \"accessToken\": \"" + authResponse.getAccessToken() + "\" }");

    }
}
