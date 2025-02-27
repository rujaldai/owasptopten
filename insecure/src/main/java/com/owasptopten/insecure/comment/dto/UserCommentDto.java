package com.owasptopten.insecure.comment.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCommentDto {
    private Long id;
    private String username;
    private String comment;
    private byte[] imageData;
    private LocalDateTime createdAt;
}
