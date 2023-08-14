package com.kaj.myapp.auth.entity;

import com.kaj.myapp.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<List<Profile>> findByUser_Id(long id);
    @Query(value = "select * from profile order by id asc", nativeQuery = true)
    List<Profile> findProfileSortByid();



}
