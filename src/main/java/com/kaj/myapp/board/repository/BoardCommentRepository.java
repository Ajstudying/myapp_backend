package com.kaj.myapp.board.repository;


import com.kaj.myapp.board.entity.BoardComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardCommentRepository extends JpaRepository <BoardComment, Long> {
    @Query("select b from BoardComment b where b.board.no = ?1 order by b.id")
    List<BoardComment> findByBoard_NoOrderByIdAsc(long no);

    @Query("select b from BoardComment b where b.ownerName = ?1")
    Optional<BoardComment> findByOwnerName(String ownerName);




}
