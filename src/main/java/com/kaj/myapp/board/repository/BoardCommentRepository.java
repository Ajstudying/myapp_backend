package com.kaj.myapp.board.repository;


import com.kaj.myapp.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCommentRepository extends JpaRepository <BoardComment, Long> {
}
