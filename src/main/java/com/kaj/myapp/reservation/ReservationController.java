package com.kaj.myapp.reservation;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import com.kaj.myapp.post.PostModifyRequest;
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
    @GetMapping(value="/{currentTime}")
    public ResponseEntity getReservation (@PathVariable long currentTime, @RequestAttribute AuthUser authUser){

        System.out.println(currentTime);
        Optional<List<Reservation>> reserve = repo.findByNickname(authUser.getNickname());
        if(!reserve.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        for (int i = 0; i < reserve.get().size(); i++) {
            System.out.println(reserve.get().get(i).getReservationTime());
            if(currentTime == reserve.get().get(i).getReservationTime()){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(reserve.get().get(i));
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

    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity removeReservation (@PathVariable long no, @RequestAttribute AuthUser authUser){
        System.out.println(no + "del");

        Optional<Reservation> reserve = repo.findByNo(no);

        if(!reserve.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(reserve.get().getNickname() == authUser.getNickname()){
            repo.deleteById(no);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyReservation (@PathVariable long no, @RequestBody ReservationModifyRequest reserve, @RequestAttribute AuthUser authUser){
        System.out.println(no + "modify");
        Optional<Reservation> reservation = repo.findByNo(no);

        if(!reservation.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Reservation modifyReserve = reservation.get();
        if(modifyReserve.getNickname() == authUser.getNickname()){
            if(reserve.getPetname() != null && !reserve.getPetname().isEmpty()){
                modifyReserve.setPetname(reserve.getPetname());
            }
            if(reserve.getReservationTime() > 0){
                modifyReserve.setReservationTime(reserve.getReservationTime());
            }
            repo.save(modifyReserve);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }




}
