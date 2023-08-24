package com.kaj.myapp.board;

import com.kaj.myapp.board.entity.Board;
import com.kaj.myapp.board.entity.BoardComment;
import com.kaj.myapp.board.repository.BoardCommentRepository;
import com.kaj.myapp.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    BoardRepository boRepo;
    @Autowired
    BoardCommentRepository commentRepo;

    @Transactional
    public void createComment(Board board, BoardComment comment){
        commentRepo.save(comment);
        boRepo.save(board);
    }

}
