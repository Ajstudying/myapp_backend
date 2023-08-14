package com.kaj.myapp.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDetailsRepository extends JpaRepository<Post, Long> {


}
