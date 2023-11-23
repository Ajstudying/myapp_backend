package com.kaj.myapp.post;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import com.kaj.myapp.auth.entity.User;
import com.kaj.myapp.auth.repository.UserRepository;
import com.kaj.myapp.post.entity.Likes;
import com.kaj.myapp.post.entity.Post;
import com.kaj.myapp.post.repository.LikesRepository;
import com.kaj.myapp.post.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Tag(name="포스트 관리 처리 API")
@RestController
@RequestMapping(value = "/api/posts")
public class PostController {

    @Autowired
    PostRepository repo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    LikesRepository likeRepo;
    @Autowired
    PostService service;

    @Operation(summary = "포스트 목록 페이징 조회")
    @GetMapping(value = "/paging")
    public Page<Post> getPostsPaging(@RequestParam int page, @RequestParam int size){
        System.out.println(page);
        System.out.println(size);

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findAll(pageRequest);
    }
    @Operation(summary = "포스트 목록 검색어 페이징 조회")
    @GetMapping(value = "/paging/search")
    public Page<Post> getPostsPagingSearch(@RequestParam int page, @RequestParam int size, @RequestParam String query){
        System.out.println(query + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findByPetnameContainsOrNicknameContains(query, query, pageRequest);
    }
//    @Operation(summary = "프로젝트 id로 프로젝트 정보 가져오기", security = [SecurityRequirement(name = "bearer-key")])
    @Operation(summary = "마이페이지의 유저 본인의 포스트 목록 조회", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @GetMapping(value = "/{nickname}")
    public ResponseEntity<Page<Post>> getPostsPagingNickname(@PathVariable String nickname, @RequestParam int page, @RequestParam int size, @RequestAttribute AuthUser authUser){
        System.out.println("닉네임 조회");
        CacheControl cacheControl = CacheControl.noCache(); // 캐시 무효화를 지시

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(cacheControl)
                .body(repo.findByNicknameOrderByNoAsc(nickname, PageRequest.of(page, size)));
    }



    @Operation(summary = "포스트 추가", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @PostMapping
    public ResponseEntity addPost(@RequestBody Post post, @RequestAttribute AuthUser authUser){

        System.out.println(post);
        System.out.println(authUser);


        if(post.getTitle() == null || post.getTitle().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        post.setCreatedTime(new Date().getTime());
        post.setNickname(authUser.getNickname());
        Optional<User> isverifyUser = userRepo.findByUserid(authUser.getUserid());
        if(!isverifyUser.isPresent()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        post.setUser(isverifyUser.get());

        Post savedPost = repo.save(post);

        if(savedPost != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.ok().build();
    }
    @Operation(summary = "포스트 삭제", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no, @RequestAttribute AuthUser authUser){
        System.out.println(no);

        ResponseEntity modifyCheckResponse = isModifyPost(no, authUser); // isModifyPost 결과 받기

        if (modifyCheckResponse.getStatusCode() != HttpStatus.OK) {
            // isModifyPost에서 Forbidden이거나 NotFound 반환 시
            return modifyCheckResponse; // 그대로 반환
        }
        Optional<Post> post = repo.findPostByNo(no);


        if(!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(post.get().getNo() != no){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Likes> likesList = likeRepo.findAllByPost_No(no);

        for(Likes likes : likesList) {
            likeRepo.delete(likes);
        }
        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "포스트 수정", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyPost(@PathVariable long no, @RequestBody PostModifyRequest post, @RequestAttribute AuthUser authUser){
        System.out.println(no);
        System.out.println(post);

        ResponseEntity modifyCheckResponse = isModifyPost(no, authUser);

        if (modifyCheckResponse.getStatusCode() != HttpStatus.OK) {

            return modifyCheckResponse;
        }
        Optional<Post> findedPost = repo.findPostByNo(no);
        if(!findedPost.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Post toModifyPost = findedPost.get();

        if(post.getTitle() != null && !post.getTitle().isEmpty()) {
            toModifyPost.setTitle(post.getTitle());
        }
        if(post.getImage() != null && !post.getImage().isEmpty()){
            toModifyPost.setImage(post.getImage());
        }
        toModifyPost.setContent(post.getContent());
        repo.save(toModifyPost);
        return ResponseEntity.ok().build();


    }
    @Operation(summary = "포스트 권한 확인", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @GetMapping(value = "/verify/{no}")
    public ResponseEntity isModifyPost (@PathVariable long no, @RequestAttribute AuthUser authUser){

        System.out.println(no + "가능한가");

        Optional<Post> findedPost = repo.findById(no);
        if(!findedPost.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        System.out.println(findedPost.get().getUser().getUserid());
        System.out.println(authUser.getUserid());

        if(findedPost.get().getUser().getUserid().equals(authUser.getUserid())){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "좋아요 목록 조회", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @GetMapping(value = "/like")
    public ResponseEntity<List<Likes>> getLikes(@RequestAttribute AuthUser authUser){
        List<Likes> likes = likeRepo.findByOwnerId(authUser.getId());
        if(likes == null || likes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(likes);
    }

    @Operation(summary = "좋아요 추가/취소", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @PutMapping(value = "/{no}/{like}")
    @Transactional
    public ResponseEntity addLike(@PathVariable long no, @PathVariable boolean like, @RequestAttribute AuthUser authUser){

        Optional<Post> post = repo.findPostByNo(no);
        if (!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Likes likes = new Likes();

        if (like) {
            System.out.println("좋아요");
            post.get().setLikeCount(post.get().getLikeCount() + 1);
            likes.setOwnerId(authUser.getId());
            likes.setLikes(true);
            likes.setPost(post.get());
            Optional<Likes> findedLike = likeRepo.findByPost_NoAndOwnerId(no, authUser.getId());
            if(!findedLike.isPresent()){
                service.createLikes(post.get(), likes);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            System.out.println("싫어요");
            Optional<Likes> findedLike = likeRepo.findByPost_NoAndOwnerId(no, authUser.getId());
            if(!findedLike.isPresent()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            likeRepo.delete(findedLike.get());
            post.get().setLikeCount(post.get().getLikeCount() - 1);
            return ResponseEntity.status(HttpStatus.OK).build();
        }


    }


}
