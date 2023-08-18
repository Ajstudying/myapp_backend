package com.kaj.myapp.board;

import com.kaj.myapp.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String nickname;
    @Column(length = 1024 * 1024 * 20)
    private String image;
    private long createdTime;

    @Column(nullable = false)
    private String petname;
    @Column(nullable = false)
    private String species;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
