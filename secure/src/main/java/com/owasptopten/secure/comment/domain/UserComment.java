package com.owasptopten.secure.comment.domain;

import com.owasptopten.secure.userdetails.domain.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name = "user_comments")
@Entity
public class UserComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String comment;

    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    private boolean internal = false;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
