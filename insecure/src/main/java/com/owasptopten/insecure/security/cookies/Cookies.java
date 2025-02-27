package com.owasptopten.insecure.security.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class Cookies {

    public static Cookie createCookie(String name, String value, Duration maxDuration) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge((int) maxDuration.getSeconds());
        cookie.setPath("/");
        return cookie;
    }

    public static void removeCookie(String key, HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie(key, "");
        cookie.setMaxAge(0);
        cookie.setPath("/"); 
        httpServletResponse.addCookie(cookie);
    }
}
