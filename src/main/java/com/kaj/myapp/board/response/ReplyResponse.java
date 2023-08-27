package com.kaj.myapp.board.response;

import com.kaj.myapp.board.entity.ReplyComment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReplyResponse {
    private List<ReplyComment> findedReply;
    private List<ReplyComment> otherReply;
}
