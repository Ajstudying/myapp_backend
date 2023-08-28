package com.kaj.myapp.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public long getPostCountByNickname(String nickname) {
        return postRepository.countByNickname(nickname);
    }
}
