package com.owasptopten.secure.userdetails.service;

import com.owasptopten.secure.errorhandling.exceptions.UserDetailException;
import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import com.owasptopten.secure.userdetails.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    private User updateUser(User user, UserDetailDto userDetailUpdateRequest) {
        User updateRequest = userDetailUpdateRequest.toUser(passwordEncoder::encode);
        BeanUtils.copyProperties(updateRequest, user, "id");
        return user;
    }

    public UserDetailDto updateUserDetail(User user) {
        return userRepository.findById(user.getId())
                .map(existingUser -> validateDuplicateUsername(existingUser, user))
                .map(existingUser -> existingUser.update(existingUser, user, passwordEncoder))
                .map(userRepository::save)
                .map(UserDetailDto::of)
                .orElseThrow(UserDetailException::notFound);
    }

    private User validateDuplicateUsername(User existingUser, User user) {
        if (!existingUser.getUsername().equals(user.getUsername())
                && userRepository.existsByUsername(user.getUsername())) {
            throw UserDetailException.userAlreadyExists();
        }
        return existingUser;
    }

}
