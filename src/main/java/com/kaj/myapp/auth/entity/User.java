package com.kaj.myapp.auth.entity;

import com.kaj.myapp.post.entity.Likes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String userid;
    @Column(length = 500)
    private String secret;
    @Column(unique = true)
    private String nickname;

}
