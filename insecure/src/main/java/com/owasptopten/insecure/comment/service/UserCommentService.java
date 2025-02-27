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
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCommentService {

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;

    private static final String FILTER_OUT_INTERNAL_COMMENT = " and uc.internal IS FALSE ";

    /**
     * This can be used for SSRF and XSS
     * @param comment
     * @param imageUrl
     * @param user
     */
    public void saveComment(@NotBlank String comment, String imageUrl, boolean isInternalComment, User user) {
        boolean internal = user.getAuthorities().contains(Roles.ADMIN) && isInternalComment;
        byte[] imageData = null;

        // Fetch image data if URL is provided
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                imageData = restTemplate.getForObject(imageUrl, byte[].class);
            } catch (Exception e) {
                // Log error but continue without image
                System.err.println("Failed to fetch image from URL: " + imageUrl);
            }
        }

        jdbcTemplate.update(
            "INSERT INTO user_comments (user_id, comment, image_data, internal, created_at) VALUES (?, ?, ?, ?, NOW())",
            user.getId(),
            comment,
            imageData,
            internal
        );
    }

    public List<UserCommentDto> getComments(String username, User user, Pageable pageable) {
        boolean includeInternalComment = user.getAuthorities().contains(Roles.ADMIN);
        if (ObjectUtils.isEmpty(username)) {
            return jdbcTemplate.query(
                    "SELECT uc.id, u.username, uc.comment, uc.image_url as imageUrl, uc.image_data as imageData, uc.created_at as createdAt " +
                            "FROM user u JOIN user_comments uc ON u.id = uc.user_id " +
                            (includeInternalComment ? "" : FILTER_OUT_INTERNAL_COMMENT) +
                            "ORDER BY uc.id DESC LIMIT ? OFFSET ?",
                    new BeanPropertyRowMapper<>(UserCommentDto.class),
                    pageable.getPageSize(),
                    pageable.getPageNumber()
            );
        }
        return getUserComments(username, includeInternalComment, pageable);
    }

    /**
     * SQL injection here
     */
    private List<UserCommentDto> getUserComments(String username, boolean includeInternalComment, Pageable pageable) {
        return jdbcTemplate.query(
                "SELECT uc.id, u.username, uc.comment, uc.image_url as imageUrl, uc.image_data as imageData, uc.created_at as createdAt " +
                        "FROM user u JOIN user_comments uc ON u.id = uc.user_id " +
                        (includeInternalComment ? "" : FILTER_OUT_INTERNAL_COMMENT) +
                        "WHERE u.username = \"" + username + "\" " +
                        "ORDER BY uc.id DESC LIMIT ? OFFSET ?",
                new BeanPropertyRowMapper<>(UserCommentDto.class),
                pageable.getPageSize(),
                pageable.getPageNumber()
        );
    }
}
