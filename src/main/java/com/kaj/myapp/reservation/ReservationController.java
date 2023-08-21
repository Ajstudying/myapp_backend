package com.kaj.myapp.reservation;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/reserve")
public class ReservationController {

    @Autowired
    ReservationRepository repo;
    @Auth
    @GetMapping
    public ResponseEntity getReservation (@RequestBody Reservation currentTime, @RequestAttribute AuthUser authUser){

        Optional<List<Reservation>> reserve = repo.findByNickname(authUser.getNickname());
        if(!reserve.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        for (int i = 0; i < reserve.get().size(); i++) {
            if(currentTime.getReservationTime() == reserve.get().get(i).getReservationTime()){
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }

        return ResponseEntity.ok().build();
    }

    @Auth
    @PostMapping
    public ResponseEntity addReservation (@RequestBody Reservation reservation, @RequestAttribute AuthUser authUser){
        System.out.println(reservation);

        if(reservation.getPetname() == null || reservation.getPetname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(reservation.getReservationTime() <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        reservation.setNickname(authUser.getNickname());
        Reservation savedReservation = repo.save(reservation);
        if(savedReservation != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.ok().build();
    }


}
