package com.owasptopten.secure.userdetails.service;

import com.owasptopten.secure.userdetails.domain.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
} 