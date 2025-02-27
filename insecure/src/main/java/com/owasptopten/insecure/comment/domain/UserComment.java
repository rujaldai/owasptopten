package com.owasptopten.insecure.comment.domain;

import com.owasptopten.insecure.userdetails.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

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
