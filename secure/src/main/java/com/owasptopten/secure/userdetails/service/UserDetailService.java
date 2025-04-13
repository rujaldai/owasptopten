package com.owasptopten.secure.userdetails.service;

import com.owasptopten.secure.errorhandling.exceptions.UserDetailException;
import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import com.owasptopten.secure.userdetails.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public List<UserDetailDto> getAllUserDetails() {
        return userRepository.findAll().stream()
                .map(UserDetailDto::of)
                .toList();
    }

    public Optional<UserDetailDto> getUserDetail(Long id) {
        return userRepository.findById(id)
                .map(UserDetailDto::of);
    }

    public UserDetailDto createUserDetail(UserDetailDto userDetailDto) {
        if (userRepository.existsByUsername(userDetailDto.username())) {
            throw UserDetailException.userAlreadyExists();
        }

        User user = userRepository.save(userDetailDto.toUser(passwordEncoder::encode));
        return UserDetailDto.of(user);
    }

    public UserDetailDto updateUserDetail(User loggedInUser, UserDetailDto userBeingUpdated) {
        if (!loggedInUser.isAdmin()) {
            userBeingUpdated = userBeingUpdated.getSanitizedInputForNonAdmin();
        }
        UserDetailDto finalUserBeingUpdated = userBeingUpdated;
        return userRepository.findById(userBeingUpdated.id())
                .map(existingUser -> validateDuplicateUsername(existingUser, finalUserBeingUpdated))
                .map(existingUser -> existingUser.update(existingUser, finalUserBeingUpdated, passwordEncoder))
                .map(userRepository::save)
                .map(UserDetailDto::of)
                .orElseThrow(UserDetailException::notFound);
    }

    private User validateDuplicateUsername(User existingUser, UserDetailDto userBeingUpdated) {
        if (!existingUser.getUsername().equals(userBeingUpdated.username())
                && userRepository.existsByUsername(userBeingUpdated.username())) {
            throw UserDetailException.userAlreadyExists();
        }
        return existingUser;
    }

}
