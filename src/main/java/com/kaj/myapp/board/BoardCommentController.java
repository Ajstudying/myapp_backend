package com.kaj.myapp.board;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import com.kaj.myapp.board.entity.Board;
import com.kaj.myapp.board.entity.BoardComment;
import com.kaj.myapp.board.entity.ReplyComment;
import com.kaj.myapp.board.repository.BoardCommentRepository;
import com.kaj.myapp.board.repository.BoardRepository;
import com.kaj.myapp.board.repository.ReplyCommentRepository;
import com.kaj.myapp.board.request.CommentModifyRequest;
import com.kaj.myapp.board.response.CommentResponse;
import com.kaj.myapp.board.response.ReplyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/boards/{no}/comments")
public class BoardCommentController {

    @Autowired
    BoardRepository boRepo;
    @Autowired
    BoardCommentRepository commentRepo;

    @Autowired
    ReplyCommentRepository replyRepo;
    @Autowired
    BoardService service;


//    @Auth
//    @GetMapping
//    public List<BoardComment> getCommentList(@PathVariable long no, @RequestAttribute AuthUser authUser) {
//        System.out.println("출력");
//
//        List<BoardComment> list = commentRepo.findBoardCommentSortById(no);
//        return list;
//    }
    @Auth
    @GetMapping
    public ResponseEntity getComment(@PathVariable long no, @RequestAttribute AuthUser authUser) {
        System.out.println("출력");

        //해당 유저의 댓글 찾기
        Optional<List<BoardComment>> comment = commentRepo.findByBoardNo(no);
        if(!comment.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<BoardComment> findedComment = new ArrayList<>();
        List<BoardComment> otherComment = new ArrayList<>();
        for (int i = 0; i < comment.get().size(); i++) {
            if(comment.get().get(i).getOwnerName().equals(authUser.getNickname())){
                findedComment.add(comment.get().get(i));
                System.out.println(comment.get().get(i).getOwnerName());
            }else {
                otherComment.add(comment.get().get(i));
            }
        }
        CommentResponse response = new CommentResponse();
        response.setFindedComment(findedComment);
        response.setOtherComment(otherComment);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Auth
    @PostMapping
    public ResponseEntity createComment(@PathVariable long no, @RequestBody BoardComment comment, @RequestAttribute AuthUser authUser) {

        Optional<Board> findedBoard = boRepo.findByNo(no);
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(comment.getContent() == null || comment.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        comment.setBoard(findedBoard.get());
        comment.setOwnerId(authUser.getId());
        comment.setOwnerName(authUser.getNickname());

        Board board = findedBoard.get();
        board.setLastestComment(comment.getContent());
        board.setCommentCnt(findedBoard.get().getCommentCnt() + 1);
        // 트랜잭션 처리
        service.createComment(board, comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
    @Auth
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteComment(@PathVariable long no, @PathVariable long id, @RequestAttribute AuthUser authUser) {
        Optional<Board> findedBoard = boRepo.findByNo(no);
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<BoardComment> comment = commentRepo.findById(id);
        if(!comment.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(comment.get().getOwnerName().equals(authUser.getNickname())){
            commentRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Auth
    @PutMapping(value = "/{id}")
    public ResponseEntity modifyComment(@PathVariable long no, @PathVariable long id, @RequestBody CommentModifyRequest comment, @RequestAttribute AuthUser authUser){
        System.out.println("수정");
        Optional<Board> findedBoard = boRepo.findByNo(no);
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<BoardComment> findedcomment = commentRepo.findById(id);
        if(!findedcomment.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(!findedcomment.get().getOwnerName().equals(authUser.getNickname())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(findedBoard.get());
        }
        if(comment.getContent() != null || !comment.getContent().isEmpty()){
            findedcomment.get().setContent(comment.getContent());
            commentRepo.save(findedcomment.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @Auth
    @GetMapping("/{id}/reply")
    public ResponseEntity getReplys(@PathVariable long id, @RequestAttribute AuthUser authUser) {

        System.out.println("댓글조회");
        Optional<List<ReplyComment>> reply = replyRepo.findByCommentId(id);
        if(!reply.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<ReplyComment> findedReply = new ArrayList<>();
        List<ReplyComment> otherReply = new ArrayList<>();
        for (int i = 0; i < reply.get().size(); i++) {
            if(reply.get().get(i).getOwnerName().equals(authUser.getNickname())){
                findedReply.add(reply.get().get(i));
            }else {
                otherReply.add(reply.get().get(i));
            }
        }
        ReplyResponse response = new ReplyResponse();
        response.setFindedReply(findedReply);
        response.setOtherReply(otherReply);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Auth
    @PostMapping("/{id}/reply")
    public ResponseEntity addReply(@PathVariable long id, @RequestBody ReplyComment reply, @RequestAttribute AuthUser authUser) {

        Optional<BoardComment> findedComment= commentRepo.findById(id);
        if(!findedComment.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(reply.getContent() == null || reply.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        reply.setCommentId(id);
        reply.setOwnerId(authUser.getId());
        reply.setOwnerName(authUser.getNickname());

        BoardComment comment = findedComment.get();
        comment.setLastestComment(reply.getContent());
        comment.setCommentCnt(findedComment.get().getCommentCnt() + 1);
        // 트랜잭션 처리
        service.createReplyComment(comment, reply);

        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }

    @Auth
    @DeleteMapping(value = "/{id}/reply/{replyId}")
    public ResponseEntity deleteComment(@PathVariable long no, @PathVariable long id, @PathVariable long replyId, @RequestAttribute AuthUser authUser) {
        Optional<Board> findedBoard = boRepo.findByNo(no);
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<BoardComment> comment = commentRepo.findById(id);
        if(!comment.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<ReplyComment> reply = replyRepo.findById(replyId);
        if(!reply.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(reply.get().getOwnerName().equals(authUser.getNickname())){
            replyRepo.deleteById(replyId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
