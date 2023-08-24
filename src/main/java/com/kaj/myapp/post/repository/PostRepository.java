package com.kaj.myapp.post.repository;

import com.kaj.myapp.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPetnameContainsOrNicknameContains(String petname, String nickname, Pageable pageable);
    Page<Post> findByPetnameContainsOrTitleContainsOrNicknameContains(String petname, String title, String nickname, Pageable pageable);

    Page<Post> findByPetnameContainsOrTitleContains(String petname, String title, Pageable pageable);
    @Query(value = "select * from post where no = :no", nativeQuery = true)
    Optional<Post> findPostByNo(long no);










}
