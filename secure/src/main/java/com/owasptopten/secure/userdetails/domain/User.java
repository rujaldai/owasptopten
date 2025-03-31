package com.owasptopten.secure.userdetails.domain;

import com.owasptopten.secure.helpers.ObjectUtil;
import com.owasptopten.secure.userdetails.enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private List<Roles> roles = new ArrayList<>();

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return List.of(roles.toArray(new Roles[0]));
    }

    public User update(User existingUser, User newUserDetail, PasswordEncoder passwordEncoder) {
        this.username = ObjectUtil.defaultIfEmpty(newUserDetail.getUsername(), existingUser.username);
        this.roles = ObjectUtil.defaultIfEmpty(newUserDetail.getRoles(), existingUser.roles);
        Optional.ofNullable(newUserDetail.getPassword())
                .map(passwordEncoder::encode)
                .ifPresent(existingUser::setPassword);
        return this;
    }


}
