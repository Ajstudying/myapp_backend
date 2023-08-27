package com.kaj.myapp.board.repository;

import com.kaj.myapp.board.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {


    Optional<List<ReplyComment>> findByCommentId(long id);
}
