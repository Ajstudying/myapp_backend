package com.kaj.myapp.board.request;

import com.kaj.myapp.board.entity.BoardComment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentResponse {
    private List<BoardComment> findedComment;
    private List<BoardComment> otherComment;
}
