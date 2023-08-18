package com.kaj.myapp.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b order by b.no")
    Page<Board> findByOrderByNoAsc(Pageable pageable);

    @Query("select b from Board b where b.nickname like concat('%', ?1, '%')")
    Page<Board> findByNicknameContains(String nickname, Pageable pageable);

    @Query("select b from Board b where b.title like concat('%', ?1, '%')")
    Page<Board> findByTitleContains(String title, Pageable pageable);

    @Query("select b from Board b where b.content like concat('%', ?1, '%')")
    Page<Board> findByContentContains(String content, Pageable pageable);

    @Query("select b from Board b where b.species like concat('%', ?1, '%')")
    Page<Board> findBySpeciesContains(String species, Pageable pageable);

    Optional<Board> findByNo(long no);











}
