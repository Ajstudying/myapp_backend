package com.kaj.myapp.post.repository;

import com.kaj.myapp.post.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("select l from Likes l where l.ownerId = ?1")
    List<Likes> findByOwnerId(long ownerId);

    @Query("SELECT l FROM Likes l WHERE l.post.no = :no")
    List<Likes> findAllByPost_No(@Param("no") Long no);





    @Query("select l from Likes l where l.post.no = ?1 and l.ownerId = ?2")
    Optional<Likes> findByPost_NoAndOwnerId(long no, long ownerId);


}
