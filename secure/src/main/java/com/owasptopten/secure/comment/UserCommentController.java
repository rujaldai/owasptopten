package com.owasptopten.secure.comment;

import com.owasptopten.secure.comment.dto.CommentDto;
import com.owasptopten.secure.comment.dto.UserCommentDto;
import com.owasptopten.secure.comment.service.UserCommentService;
import com.owasptopten.secure.userdetails.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Secure Comment Controller with:
 * - Input validation
 * - XSS prevention
 * - SQL injection prevention
 * - Rate limiting
 * - Access control
 * - Audit logging
 */
@RestController
@RequestMapping("/api/users/comments")
@RequiredArgsConstructor
public class UserCommentController {

    private final UserCommentService userCommentService;

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).ROLE_USER)")
    @PostMapping
    public void createComment(
            @Valid @RequestBody CommentDto commentDto,
            @RequestParam(required = false) boolean internal,
            @AuthenticationPrincipal User user
    ) {
        userCommentService.saveComment(commentDto.comment(), internal, user);
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.secure.userdetails.enums.Roles).ROLE_USER)")
    @GetMapping
    public List<UserCommentDto> getComments(
            @RequestParam(required = false) String username,
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        return userCommentService.getComments(username, user, pageable);
    }
} 