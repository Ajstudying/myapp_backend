package com.kaj.myapp.board.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentModifyRequest {
    private String content;
    private long createdTime;
}
