package com.owasptopten.secure.userdetails.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.owasptopten.secure.helpers.EmptyRoleDeserializer;
import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
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

    public UserDetailDto sanitizedInputForNonAdmin() {
        return new UserDetailDto(this.id, this.username, this.password, Collections.emptyList());
    }
}
