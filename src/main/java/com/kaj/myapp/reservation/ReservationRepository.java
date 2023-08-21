package com.kaj.myapp.reservation;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Long> {
    @Query("select r from Reservation r where r.no = ?1")
    Optional<Reservation> findByNo(long no);



    Optional<List<Reservation>> findByNickname(String nickname);

//    @Query("select r from Reservation r where r.nickname = ?1")
//    Optional<Reservation> findByNickname(String nickname);



}
