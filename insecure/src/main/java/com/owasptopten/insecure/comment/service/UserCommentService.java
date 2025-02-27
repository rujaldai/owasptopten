package com.owasptopten.insecure.comment.service;

import com.owasptopten.insecure.comment.dto.UserCommentDto;
import com.owasptopten.insecure.userdetails.domain.User;
import com.owasptopten.insecure.userdetails.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCommentService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * This can be used for XSS
     * @param comment
     * @param user
     */
    public void saveComment(@NotBlank String comment, boolean isInternalComment, User user) {
        boolean internal = user.getAuthorities().contains(Roles.ADMIN) && isInternalComment;
        jdbcTemplate.update("INSERT INTO user_comments (user_id, comment, internal) VALUES (\""
                + user.getId()
                + "\", \""
                + comment
                + "\", "
                + internal
                + ")");
    }

    public List<UserCommentDto> getComments(String username, User user, Pageable pageable) {
        boolean doNotIncludeInternalComment = !user.getAuthorities().contains(Roles.ADMIN);
        if (ObjectUtils.isEmpty(username)) {
            return jdbcTemplate.query("SELECT u.username, uc.comment FROM user u JOIN user_comments uc ON u.id = uc.user_id and uc.internal = ? ORDER BY uc.id DESC LIMIT ? OFFSET ? ", new BeanPropertyRowMapper<>(UserCommentDto.class), doNotIncludeInternalComment, pageable.getPageSize(), pageable.getPageNumber());
        }
        return getUserComments(username, doNotIncludeInternalComment, pageable);
    }

    /**
     * SQL injection here
     *
     * @param username
     * @param pageable
     * @return
     */
    private List<UserCommentDto> getUserComments(String username, boolean includeInternalComment, Pageable pageable) {
        return jdbcTemplate.query("SELECT u.username, uc.comment FROM user u JOIN user_comments uc ON u.id = uc.user_id WHERE uc.internal = ? and u.username = \"" + username + "\" ORDER BY uc.id DESC LIMIT ? OFFSET ? ;", new BeanPropertyRowMapper<>(UserCommentDto.class), includeInternalComment, pageable.getPageSize(), pageable.getPageNumber());
    }
}
