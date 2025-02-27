package com.owasptopten.insecure.userdetails.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.owasptopten.insecure.helpers.EmptyRoleDeserializer;
import com.owasptopten.insecure.userdetails.domain.User;
import com.owasptopten.insecure.userdetails.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record UserDetailDto(Long id,
                            @NotBlank String username,
                            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                            @NotBlank @NotNull String password,
                            @JsonDeserialize(using = EmptyRoleDeserializer.class)
                            List<Roles> roles) {

    public UserDetailDto(Long id, String username, List<Roles> roles) {
        this(id, username, null, roles);
    }

    public static UserDetailDto of(User user) {
        return new UserDetailDto(user.getId(), user.getUsername(), user.getRoles());
    }

    public User toUser(Function<String, String> passwordEncoder) {
        User user = new User();
        user.setUsername(this.username);
        user.setRoles(this.roles);
        Optional.ofNullable(this.password)
                .map(passwordEncoder)
                .ifPresent(user::setPassword);
        return user;
    }
}
