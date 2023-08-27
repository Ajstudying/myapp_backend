package com.kaj.myapp.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private long createdTime;

    @ManyToOne
    private Board board;

    private long ownerId; //댓글 다는 사람의 아이디
    private String ownerName; //댓글을 다는 사람들의 닉네임

    //댓글 수
    private long commentCnt;
    //최근 댓글 내용
    private String lastestComment;
}
