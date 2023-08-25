package com.kaj.myapp.board;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import com.kaj.myapp.board.entity.Board;
import com.kaj.myapp.board.repository.BoardRepository;
import com.kaj.myapp.board.request.BoardModifyRequest;
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

    @Auth
    @GetMapping(value = "/{boardNo}")
    public ResponseEntity getBoard(@PathVariable long boardNo, @RequestAttribute AuthUser authUser) {

        Optional<Board> findedBoard = boRepo.findById(boardNo);
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(findedBoard.get().getNickname().equals(authUser.getNickname())){
            return ResponseEntity.status(HttpStatus.OK).body(findedBoard.get());
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(findedBoard.get());
        }
    }
    @GetMapping(value = "/paging")
    public Page<Board> getBoardsPaging(@RequestParam int page, @RequestParam int size){
        System.out.println(page + "1");
        System.out.println(size + "1");

        return boRepo.findByOrderByNoDesc(PageRequest.of(page, size));
    }

    @GetMapping(value = "paging/search")
    public Page<Board> getBoardsPagingSearch(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String species) {

        System.out.println("검색");
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
        if(board.getSpecies() == null || board.getSpecies().isEmpty()){
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

        Optional<Board> removeBoard = boRepo.findById(no);
        if(!removeBoard.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boRepo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyBoard(@PathVariable long no, @RequestBody BoardModifyRequest board, @RequestAttribute AuthUser authUser){
        System.out.println(no + "8");

        Optional<Board> findedBoard = boRepo.findByNo(no);
        if(!findedBoard.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Board toModifyBoard = findedBoard.get();

        if(board.getTitle() != null && !board.getTitle().isEmpty()){
            toModifyBoard.setTitle(board.getTitle());
        }
        if(board.getContent() != null && !board.getContent().isEmpty()){
            toModifyBoard.setContent(board.getContent());
        }
        if(board.getPetname() != null && !board.getPetname().isEmpty()){
            toModifyBoard.setPetname(board.getPetname());
        }
        toModifyBoard.setSpecies(board.getSpecies());
        toModifyBoard.setImage(board.getImage());
        boRepo.save(toModifyBoard);
        return ResponseEntity.ok().build();
    }

}
