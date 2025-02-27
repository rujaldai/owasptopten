package com.owasptopten.insecure.security.authentication.controller;

import com.owasptopten.insecure.security.dto.LoginRequest;
import com.owasptopten.insecure.security.authentication.service.AuthenticationService;
import com.owasptopten.insecure.userdetails.dto.UserDetailDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
