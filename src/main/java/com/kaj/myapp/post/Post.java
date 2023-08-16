package com.kaj.myapp.post;

import com.kaj.myapp.auth.entity.Profile;
import com.kaj.myapp.auth.entity.ProfileModifyRequest;
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
    @Column(nullable = false)
    private String content;
    private String nickname;
    @Column(length = 1024 * 1024 * 20)
    private String image;
    private long createdTime;
    private String petname;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}