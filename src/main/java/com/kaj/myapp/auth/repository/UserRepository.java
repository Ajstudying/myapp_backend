package com.kaj.myapp.auth.repository;

import com.kaj.myapp.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.userid = ?1")
    Optional<User> findByUserid(String userid);
//    Optional<User> findByUserid(String userid);



}
