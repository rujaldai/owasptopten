package com.owasptopten.secure.security.authentication.filter;

import com.owasptopten.secure.helpers.ObjectUtil;
import com.owasptopten.secure.security.authentication.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    private static final String[] EXCLUDED_URLS = {"/api/login", "/api/logout"};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.asList(EXCLUDED_URLS).contains(path);
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Arrays.stream(ObjectUtil.defaultIfEmpty(request.getCookies(), new Cookie[0]))
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .ifPresent(token -> validateToken(request, token));

        chain.doFilter(request, response);
    }

    private void validateToken(ServletRequest request, String token) {
        authenticationService.authenticate((HttpServletRequest) request, token);
    }
}
