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

    @ManyToOne
    private Board board;

    private long ownerId;
    private String ownerName; //댓글을 다는 사람들의 닉네임
}
