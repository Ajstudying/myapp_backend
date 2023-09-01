package com.kaj.myapp.board;

import com.kaj.myapp.board.entity.Board;
import com.kaj.myapp.board.entity.BoardComment;
import com.kaj.myapp.board.entity.ReplyComment;
import com.kaj.myapp.board.repository.BoardCommentRepository;
import com.kaj.myapp.board.repository.BoardRepository;
import com.kaj.myapp.board.repository.ReplyCommentRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Tag(name="게시판 트랜젝션 처리 서비스")
@Service
public class BoardService {

    @Autowired
    BoardRepository boRepo;
    @Autowired
    BoardCommentRepository commentRepo;

    @Autowired
    ReplyCommentRepository replyRepo;

    @Transactional
    public void createComment(Board board, BoardComment comment){
        commentRepo.save(comment);
        boRepo.save(board);
    }
    @Transactional
    public void createReplyComment(BoardComment comment, ReplyComment reply){
        replyRepo.save(reply);
        commentRepo.save(comment);
    }

    public long getBoardCountByNickname(String nickname){
        return boRepo.countByNickname(nickname);
    }


}
