package com.owasptopten.secure.userdetails.controller;

import com.owasptopten.secure.errorhandling.exceptions.UserDetailException;
import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import com.owasptopten.secure.userdetails.service.UserDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PreAuthorize("hasPermission(#id, 'User', T(com.owasptopten.secure.userdetails.enums.Roles).USER)")
    @GetMapping("/{id}")
    UserDetailDto getUserDetail(@PathVariable Long id) {
        return userDetailService.getUserDetail(id)
                .orElseThrow(UserDetailException::notFound);
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).USER)")
    @GetMapping("/current")
    UserDetailDto getCurrentUserDetail(@AuthenticationPrincipal User user) {
        return UserDetailDto.of(user);
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).ADMIN)")
    @PostMapping
    UserDetailDto createUserDetail(@RequestBody @Valid UserDetailDto userDetailDto) {
        return userDetailService.createUserDetail(userDetailDto);
    }

    @PreAuthorize("hasPermission(#userDetailDto?.id, 'User', T(com.owasptopten.secure.userdetails.enums.Roles).USER)")
    @PutMapping
    UserDetailDto updateUserDetail(@RequestBody @Valid UserDetailDto userDetailDto,
                                   @AuthenticationPrincipal User loggedInUser) {
        return userDetailService.updateUserDetail(loggedInUser, userDetailDto);
    }

}
