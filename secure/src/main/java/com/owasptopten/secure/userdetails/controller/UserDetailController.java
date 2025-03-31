package com.owasptopten.secure.userdetails.controller;

import com.owasptopten.secure.errorhandling.exceptions.UserDetailException;
import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import com.owasptopten.secure.userdetails.service.UserDetailService;
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

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).ADMIN)")
    @GetMapping
    List<UserDetailDto> getUserDetails() {
        return userDetailService.getAllUserDetails();
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).ADMIN)")
    @GetMapping("/{id}")
    UserDetailDto getUserDetail(@PathVariable Long id) {
        return userDetailService.getUserDetail(id)
                .orElseThrow(UserDetailException::notFound);
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).USER)")
    @PostMapping
    UserDetailDto createUserDetail(@RequestBody @Valid UserDetailDto userDetailDto) {
        return userDetailService.createUserDetail(userDetailDto);
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).USER)")
    @PutMapping
    UserDetailDto updateUserDetail(@RequestBody @Valid User user) {
        return userDetailService.updateUserDetail(user);
    }

}
