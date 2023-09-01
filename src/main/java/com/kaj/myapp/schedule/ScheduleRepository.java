package com.kaj.myapp.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository <Schedule, Long> {
    @Query("select r from Schedule r where r.no = ?1")
    Optional<Schedule> findByNo(long no);

    //SELECT COUNT(*) FROM your_table_name WHERE id = your_id_value;
    @Query("SELECT COUNT(*) FROM Schedule WHERE nickname = :nickname")
    Long getScheduleCountByNickname(@Param("nickname") String nickname);

    Optional<List<Schedule>> findByNickname(String nickname);

//    @Query("select r from Reservation r where r.nickname = ?1")
//    Optional<Reservation> findByNickname(String nickname);



}
