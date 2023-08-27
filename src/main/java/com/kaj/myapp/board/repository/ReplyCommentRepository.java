package com.kaj.myapp.board.repository;

import com.kaj.myapp.board.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {
}
