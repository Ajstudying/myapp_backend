package com.kaj.myapp.board.entity;

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

public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String request;
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

    //댓글 수
    private long commentCnt;
    //최근 댓글 내용
    private String lastestComment;
}
