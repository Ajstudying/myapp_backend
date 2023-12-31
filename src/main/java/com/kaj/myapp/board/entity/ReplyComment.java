package com.kaj.myapp.board.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ReplyComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyId;

    private String content;

    private long createdTime;

    private long commentId; //원댓글의 id
    private long ownerId; /* 댓글 다는 사람의 id */private String ownerName; //댓글을 다는 사람들의 닉네임
}
