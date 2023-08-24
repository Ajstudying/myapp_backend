package com.kaj.myapp.reservation;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/reserve")
public class ReservationController {

    @Autowired
    ReservationRepository repo;
    @Auth
    @GetMapping
    public ResponseEntity getReservation (@RequestAttribute AuthUser authUser){

        Optional<List<Reservation>> reserve = repo.findByNickname(authUser.getNickname());
        if(!reserve.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Reservation> list = new ArrayList<>();
        for (int i = 0; i < reserve.get().size(); i++) {
            System.out.println(reserve.get().get(i).getReservationTime());
            list.add(reserve.get().get(i));
        }
        return ResponseEntity.ok().body(list);
    }

    @Auth
    @PostMapping
    public ResponseEntity addReservation (@RequestBody Reservation reservation, @RequestAttribute AuthUser authUser){
        System.out.println(reservation);

        if(reservation.getPetname() == null || reservation.getPetname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(reservation.getContent() == null || reservation.getContent().isEmpty()){
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
        if(reserve.get().getNickname().equals(authUser.getNickname())){
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
        if(modifyReserve.getNickname().equals(authUser.getNickname())){
            if(reserve.getPetname() != null && !reserve.getPetname().isEmpty()){
                modifyReserve.setPetname(reserve.getPetname());
            }
            if(reserve.getContent() != null && !reserve.getContent().isEmpty()){
                modifyReserve.setContent(reserve.getContent());
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
