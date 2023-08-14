package com.kaj.myapp.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/postDetails")
public class PostDetails {


    @Autowired
    PostDetailsRepository postRepo;

    @PostMapping
    public ResponseEntity addPost(@RequestBody Post post){

        System.out.println(post);

        if(post.getTitle() == null || post.getTitle().isEmpty() || post.getContent() == null || post.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        post.setCreatedTime(new Date().getTime());
        Post savedPost = postRepo.save(post);

        if(savedPost != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.ok().build();
    }

}
