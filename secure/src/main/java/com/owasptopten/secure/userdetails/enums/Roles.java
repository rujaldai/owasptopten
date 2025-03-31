package com.owasptopten.secure.userdetails.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    USER, ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public static Roles from(String role) {
        return valueOf(role.toUpperCase());
    }

    public String roleName() {
        return "ROLE_" + this.name();
    }
}
