package com.owasptopten.insecure.comment;

import com.owasptopten.insecure.comment.dto.CommentDto;
import com.owasptopten.insecure.comment.dto.UserCommentDto;
import com.owasptopten.insecure.comment.service.UserCommentService;
import com.owasptopten.insecure.userdetails.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SQL Injection, XSS & SSRF
 */
@Controller
@RequestMapping("/api/users/comments")
@RequiredArgsConstructor
public class UserCommentController {

    private final UserCommentService userCommentService;

    @PreAuthorize("hasAuthority(T(com.owasptopten.insecure.userdetails.enums.Roles).USER)")
    @PostMapping
    public String createComment(CommentDto commentDto,
                                @RequestParam(required = false) boolean internal,
                                @AuthenticationPrincipal User user) {
        userCommentService.saveComment(commentDto.comment(), commentDto.imageUrl(), internal, user);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority(T(com.owasptopten.insecure.userdetails.enums.Roles).USER)")
    @GetMapping
    @ResponseBody
    public List<UserCommentDto> getComments(@RequestParam(required = false) String username,
                                            @AuthenticationPrincipal User user,
                                            Pageable pageable) {
        return userCommentService.getComments(username, user, pageable);
    }
}
