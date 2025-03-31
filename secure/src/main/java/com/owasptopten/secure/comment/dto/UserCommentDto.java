package com.owasptopten.secure.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserCommentDto {
    private Long id;
    private String username;
    private String comment;
    private byte[] imageData;
    private LocalDateTime createdAt;
}
