package com.owasptopten.secure.security.authentication.controller;

import com.owasptopten.secure.security.dto.LoginRequest;
import com.owasptopten.secure.security.authentication.service.AuthenticationService;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserDetailDto> login(
            HttpServletResponse response,
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(response, loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken() {
        // If the request reaches here, it means the token is valid
        // (validated by JwtAuthenticationFilter)
        return ResponseEntity.ok().build();
    }
} 