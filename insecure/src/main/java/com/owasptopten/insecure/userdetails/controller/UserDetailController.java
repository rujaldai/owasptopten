package com.owasptopten.insecure.userdetails.controller;

import com.owasptopten.insecure.errorhandling.exceptions.UserDetailException;
import com.owasptopten.insecure.userdetails.domain.User;
import com.owasptopten.insecure.userdetails.dto.UserDetailDto;
import com.owasptopten.insecure.userdetails.service.UserDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserDetailController {

    private final UserDetailService userDetailService;

    @PreAuthorize("hasAuthority(T(com.owasptopten.insecure.userdetails.enums.Roles).ADMIN)")
    @GetMapping
    List<UserDetailDto> getUserDetails() {
        return userDetailService.getAllUserDetails();
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.insecure.userdetails.enums.Roles).ADMIN)")
    @GetMapping("/{id}")
    UserDetailDto getUserDetail(@PathVariable Long id) {
        return userDetailService.getUserDetail(id)
                .orElseThrow(UserDetailException::notFound);
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.insecure.userdetails.enums.Roles).USER)")
    @PostMapping
    UserDetailDto createUserDetail(@RequestBody @Valid UserDetailDto userDetailDto) {
        return userDetailService.createUserDetail(userDetailDto);
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.insecure.userdetails.enums.Roles).USER)")
    @PutMapping
    UserDetailDto updateUserDetail(@RequestBody @Valid User user) {
        return userDetailService.updateUserDetail(user);
    }

}
