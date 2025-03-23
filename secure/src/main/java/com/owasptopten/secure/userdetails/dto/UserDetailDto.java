package com.owasptopten.secure.userdetails.dto;

import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.enums.Roles;

import java.util.Set;

public record UserDetailDto(
    Long id,
    String username,
    Set<Roles> roles,
    boolean enabled
) {
    public static UserDetailDto of(User user) {
        return new UserDetailDto(
            user.getId(),
            user.getUsername(),
            user.getRoles(),
            user.isEnabled()
        );
    }
} 