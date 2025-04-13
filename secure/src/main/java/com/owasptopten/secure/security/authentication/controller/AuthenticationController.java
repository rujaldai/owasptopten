package com.owasptopten.secure.security.authentication.controller;

import com.owasptopten.secure.security.authentication.service.AuthenticationService;
import com.owasptopten.secure.security.dto.LoginRequest;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("login")
    public UserDetailDto login(HttpServletResponse httpServletResponse,
                               @RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(httpServletResponse, loginRequest);
    }

    @PostMapping("logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        authenticationService.logout(httpServletRequest, httpServletResponse);
    }

}
