package com.kaj.myapp.board;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import com.kaj.myapp.board.entity.Board;
import com.kaj.myapp.board.entity.BoardComment;
import com.kaj.myapp.board.repository.BoardCommentRepository;
import com.kaj.myapp.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class BoardCommentController {

    @Autowired
    private BoardRepository boRepo;
    @Autowired
    private BoardCommentRepository commentRepo;
    @Autowired
    private BoardService service;

    @Auth
    @GetMapping(value = "/{no}")
    public List<BoardComment> getComment(@PathVariable long no, @RequestAttribute AuthUser authUser) {
        List<BoardComment> list = commentRepo.findByBoard_NoOrderByIdAsc(no);

        return list;
    }

    @Auth
    @PostMapping(value = "/{no}")
    public ResponseEntity createComment(@PathVariable long no, @RequestBody BoardComment comment, @RequestAttribute AuthUser authUser) {

        Optional<Board> findedBoard = boRepo.findByNo(no);
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        comment.setBoard(findedBoard.get());
        if(comment.getOwnerName() == null || comment.getOwnerName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(comment.getContent() == null || comment.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        BoardComment newComment = commentRepo.save(comment);
        if(newComment != null){
            service.createComment(comment.getBoard(), comment);
            System.out.println("success");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.ok().build();
    }

}
