package com.kaj.myapp.auth.repository;

import com.kaj.myapp.auth.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<List<Profile>> findByUser_Id(long id);

//    @Query(value = "select * from profile order by id asc", nativeQuery = true)
//    List<Profile> findProfileSortByid();

}
