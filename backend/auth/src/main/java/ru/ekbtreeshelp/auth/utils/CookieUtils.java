package ru.ekbtreeshelp.auth.utils;

import ru.ekbtreeshelp.auth.constants.AuthConstants;
import ru.ekbtreeshelp.auth.constants.CookieNames;
import ru.ekbtreeshelp.auth.dto.JwtDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

public class CookieUtils {

    public static void setTokenCookies(HttpServletResponse response, JwtDto tokens) {
        Cookie accessTokenCookie = new Cookie(CookieNames.ACCESS_TOKEN, tokens.getAccessToken());
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(AuthConstants.ACCESS_TOKEN_MAX_AGE));
        response.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie(CookieNames.REFRESH_TOKEN, tokens.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(AuthConstants.REFRESH_TOKEN_MAX_AGE));
        response.addCookie(refreshTokenCookie);
    }

    public static void removeCookies(HttpServletResponse response, String... cookieNames) {
        Stream.of(cookieNames).forEach(cookieName -> {
            Cookie cookie = new Cookie(cookieName, "");
            cookie.setHttpOnly(false);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
    }
}
