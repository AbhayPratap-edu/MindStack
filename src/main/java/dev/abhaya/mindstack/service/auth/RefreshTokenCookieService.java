package dev.abhaya.mindstack.service.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieService {

    private static final String COOKIE_NAME = "refresh_token";
    private static final String COOKIE_PATH = "/auth/refresh";
    private static final int MAX_AGE = 7 * 24 * 60 * 60;

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {

        Cookie cookie = new Cookie(COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);//JavaScript cannot access this cookie. Only browser automatically sends it in requests
        cookie.setSecure(true);//sent over only HTTPS. not over HTTP
        cookie.setPath(COOKIE_PATH);//Browser Send this cookie only when request path starts with /auth/refresh.
        cookie.setMaxAge(MAX_AGE);//Cookie persists for 7 days,should match refresh token validation time
        cookie.setAttribute("SameSite", "Strict");//Cookie is sent ONLY when request originates from same site.

        response.addCookie(cookie);//attaches it to HTTP response header

    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");

        response.addCookie(cookie);
    }
}
