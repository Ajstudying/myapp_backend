package com.kaj.myapp.board;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import com.kaj.myapp.auth.entity.UserRepository;
import com.kaj.myapp.post.Post;
import com.kaj.myapp.post.PostModifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(value = "/boards")
public class BoardController {

    @Autowired
    BoardRepository boRepo;
    @Autowired
    UserRepository usRepo;

    @GetMapping(value = "/paging")
    public Page<Board> getBoardsPaging(@RequestParam int page, @RequestParam int size){
        System.out.println(page + "1");
        System.out.println(size + "1");

        return boRepo.findByOrderByNoAsc(PageRequest.of(page, size));
    }

    @GetMapping(value = "paging/search")
    public Page<Board> getBoardsPagingSearch(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String species) {

        if (nickname != null) {
            return boRepo.findByNicknameContains(nickname, PageRequest.of(page, size));
        } else if (title != null) {
            return boRepo.findByTitleContains(title, PageRequest.of(page, size));
        } else if (content != null) {
            return boRepo.findByContentContains(content, PageRequest.of(page, size));
        } else if (species != null) {
            return boRepo.findBySpeciesContains(species, PageRequest.of(page, size));
        } else {
            return boRepo.findByOrderByNoAsc(PageRequest.of(page, size));
        }
    }

    @Auth
    @PostMapping
    public ResponseEntity addBoard (@RequestBody Board board, @RequestAttribute AuthUser authUser){
        System.out.println(6);

        if(board.getTitle() == null || board.getTitle().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(board.getContent() == null || board.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(board.getPetname() == null || board.getPetname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        board.setNickname(authUser.getNickname());
        board.setCreatedTime(new Date().getTime());

        Board savedBoard = boRepo.save(board);

        if(savedBoard != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.ok().build();
    }

    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity removeBoard(@PathVariable long no, @RequestAttribute AuthUser authUser){

        System.out.println(no + "7");

        Optional<Board> removeBoard = boRepo.findById(new BoardId(authUser.getId(), authUser.getNickname()));
        if(!removeBoard.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(removeBoard.get().getNo() != no){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boRepo.deleteById(new BoardId(authUser.getId(), authUser.getNickname()));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyBoard(@PathVariable long no, @RequestBody BoardModifyRequest board, @RequestAttribute AuthUser authUser){
        System.out.println(no + "8");

        Optional<Board> findedBoard = boRepo.findById(new BoardId(authUser.getId(), authUser.getNickname()));
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Board toModifyBoard = findedBoard.get();

        if(board.getTitle() != null && board.getTitle().isEmpty()){
            toModifyBoard.setTitle(board.getTitle());
        }
        if(board.getContent() != null && board.getContent().isEmpty()){
            toModifyBoard.setContent(board.getContent());
        }
        if(board.getPetname() != null && board.getPetname().isEmpty()){
            toModifyBoard.setPetname(board.getPetname());
        }
        boRepo.save(toModifyBoard);
        return ResponseEntity.ok().build();
    }

}
