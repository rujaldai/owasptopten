package com.owasptopten.secure.security.authentication.service;

import com.owasptopten.secure.helpers.ObjectUtil;
import com.owasptopten.secure.security.cookies.Cookies;
import com.owasptopten.secure.security.dto.LoginRequest;
import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>(); // TODO: move to redis

    public UserDetailDto authenticate(HttpServletResponse httpServletResponse, LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.username(), loginRequest.password());
        return authenticate(httpServletResponse, authenticationRequest);
    }

    public UserDetailDto authenticate(HttpServletResponse httpServletResponse, UsernamePasswordAuthenticationToken authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = UUID.randomUUID().toString();
        Cookie cookie = Cookies.createCookie("token", token, Duration.ofMinutes(5));
        httpServletResponse.addCookie(cookie);

        tokens.put(token, authenticationRequest.getName());

        return UserDetailDto.of((User) authentication.getPrincipal());
    }

    public void authenticate(HttpServletRequest request, String token) {
        String username = tokens.get(token);
        if (username == null) {
            return;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) {
        Arrays.stream(ObjectUtil.defaultIfEmpty(httpServletRequest.getCookies(), new Cookie[0]))
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst()
                .ifPresent(tokens::remove);

        Cookies.removeCookie("token", httpServletResponse);
    }
}
