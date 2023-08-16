package com.kaj.myapp.post;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import com.kaj.myapp.auth.entity.Profile;
import com.kaj.myapp.auth.entity.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    @Autowired
    PostRepository repo;
    @Autowired
    ProfileRepository proRepo;

    @GetMapping
    public List<Post> getPostList() {
        List<Post> list = repo.findAll(Sort.by("no").ascending());
        return list;
    }

    @GetMapping(value = "/paging")
    public Page<Post> getPostsPaging(@RequestParam int page, @RequestParam int size){
        System.out.println(page);
        System.out.println(size);

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findAll(pageRequest);
    }
    @GetMapping(value = "/paging/search")
    public Page<Post> getPostsPagingSearch(@RequestParam int page, @RequestParam int size, @RequestParam String query){
        System.out.println(page + "1");
        System.out.println(size + "1");
        System.out.println(query + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findByPetnameContainsOrTitleContainsOrNicknameContains(query, query, query, pageRequest);
    }



    @Auth
    @PostMapping
    public ResponseEntity addPost(@RequestBody Post post, @RequestAttribute AuthUser authUser){

        System.out.println(post);
        System.out.println(authUser);


        if(post.getTitle() == null || post.getTitle().isEmpty() || post.getContent() == null || post.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        post.setCreatedTime(new Date().getTime());
        post.setNickname(authUser.getNickname());
        Optional<List<Profile>> profileList =  proRepo.findByUser_Id(authUser.getId());
        if(!profileList.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        for (int i = 0; i < profileList.get().size(); i++) {
            if(profileList.get().get(i).getPetname() == post.getPetname()) {
                post.setProfile(profileList.get().get(i));
            }
        }

        Post savedPost = repo.save(post);

        if(savedPost != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.ok().build();
    }
    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no, @RequestAttribute AuthUser authUser){
        System.out.println(no);

        Optional<Post> post = repo.findPostByNo(no);
        Optional<Post> authUserPost = repo.findPostByNo(authUser.getId());
        if(post != authUserPost){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(!post.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(post.get().getNo() != no){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyPost(@PathVariable long no, @RequestBody PostModifyRequest post, @RequestAttribute AuthUser authUser){
        System.out.println(no);
        System.out.println(post);

        Optional<Post> findedPost = repo.findById(authUser.getId());
        if(!findedPost.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Post toModifyPost = findedPost.get();

        if(post.getTitle() != null && !post.getTitle().isEmpty()){
            toModifyPost.setTitle(post.getTitle());
        }
        if(post.getContent() != null && !post.getContent().isEmpty()){
            toModifyPost.setContent(post.getContent());
        }
        if(post.getImage() != null && !post.getImage().isEmpty()){
            toModifyPost.setImage(post.getImage());
        }

        repo.save(toModifyPost);

        return ResponseEntity.ok().build();
    }




}
