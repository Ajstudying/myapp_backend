package com.kaj.myapp.board;

import com.kaj.myapp.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@IdClass(BoardId.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;
    @Id
    private String nickname;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
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
