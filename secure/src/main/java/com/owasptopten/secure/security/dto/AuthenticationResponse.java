package com.owasptopten.secure.security.dto;

public record AuthenticationResponse(
    String token,
    String refreshToken
) {} 