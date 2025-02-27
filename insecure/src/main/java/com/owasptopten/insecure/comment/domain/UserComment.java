package com.owasptopten.insecure.comment.domain;

import com.owasptopten.insecure.userdetails.domain.User;
import jakarta.persistence.*;

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

    private boolean internal = false;

}
