package com.owasptopten.secure.security.cookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class SecureCookies {

    public static Cookie createSecureCookie(String name, String value, Duration maxDuration) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge((int) maxDuration.getSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(true);  // Prevent XSS attacks
        cookie.setSecure(true);    // Only send over HTTPS
        cookie.setAttribute("SameSite", "Strict");  // Prevent CSRF attacks
        return cookie;
    }

    public static void removeCookie(String key, HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie(key, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Strict");
        httpServletResponse.addCookie(cookie);
    }

    public static void addSecurityHeaders(HttpServletResponse response) {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }
} 