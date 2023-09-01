package com.kaj.myapp.post.entity;

import com.kaj.myapp.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    @Column(nullable = false)
    private String title;
    private String content;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false, length = 1024 * 1024 * 20)
    private String image;
    private long createdTime;
    @Column(nullable = false)
    private String petname;

    private long likeCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}