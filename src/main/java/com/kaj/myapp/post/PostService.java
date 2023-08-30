package com.kaj.myapp.post;

import com.kaj.myapp.post.entity.Likes;
import com.kaj.myapp.post.entity.Post;
import com.kaj.myapp.post.repository.LikesRepository;
import com.kaj.myapp.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private LikesRepository likesRepo;

    @Transactional
    public void createLikes(Post post, Likes likes){
        postRepo.save(post);
        likesRepo.save(likes);
    }

    public long getPostCountByNickname(String nickname) {
        return postRepo.countByNickname(nickname);
    }
}
