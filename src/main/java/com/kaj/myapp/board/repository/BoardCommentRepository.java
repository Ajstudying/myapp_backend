package com.kaj.myapp.board.repository;


import com.kaj.myapp.board.entity.BoardComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardCommentRepository extends JpaRepository <BoardComment, Long> {
    @Query(value = "select * from board_comment where board_no = :no order by id asc", nativeQuery = true)
    List<BoardComment> findBoardCommentSortById(@Param("no") long no);

    Optional<List<BoardComment>> findByBoardNo(long no);

    Optional<BoardComment> findById(long id);




}
