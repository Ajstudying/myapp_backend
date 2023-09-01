package com.kaj.myapp.schedule;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name="일정 관리 처리 API")
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

    @Autowired
    ScheduleRepository repo;

    @Operation(summary = "일정 조회", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @GetMapping
    public ResponseEntity getSchedule (@RequestAttribute AuthUser authUser){

        Optional<List<Schedule>> schedule = repo.findByNickname(authUser.getNickname());
        if(!schedule.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Schedule> list = new ArrayList<>();
        for (int i = 0; i < schedule.get().size(); i++) {
            System.out.println(schedule.get().get(i).getReservationTime());
            list.add(schedule.get().get(i));
        }
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "일정 추가", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @PostMapping
    public ResponseEntity addSchedule (@RequestBody Schedule schedule, @RequestAttribute AuthUser authUser){
        System.out.println(schedule);

        if(schedule.getPetname() == null || schedule.getPetname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(schedule.getContent() == null || schedule.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(schedule.getReservationTime() <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        schedule.setNickname(authUser.getNickname());
        Schedule savedSchedule = repo.save(schedule);
        if(savedSchedule != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일정 삭제", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity removeSchedule (@PathVariable long no, @RequestAttribute AuthUser authUser){
        System.out.println(no + "del");

        Optional<Schedule> schedule = repo.findByNo(no);

        if(!schedule.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(schedule.get().getNickname().equals(authUser.getNickname())){
            repo.deleteById(no);
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @Operation(summary = "일정 수정", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifySchedule (@PathVariable long no, @RequestBody ScheduleModifyRequest schedule, @RequestAttribute AuthUser authUser){
        System.out.println(no + "modify");
        Optional<Schedule> findedSchedule = repo.findByNo(no);

        if(!findedSchedule.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Schedule modifyschedule = findedSchedule.get();
        if(modifyschedule.getNickname().equals(authUser.getNickname())){
            if(schedule.getPetname() != null && !schedule.getPetname().isEmpty()){
                modifyschedule.setPetname(schedule.getPetname());
            }
            if(schedule.getContent() != null && !schedule.getContent().isEmpty()){
                modifyschedule.setContent(schedule.getContent());
            }
            if(schedule.getReservationTime() > 0){
                modifyschedule.setReservationTime(schedule.getReservationTime());
            }
            repo.save(modifyschedule);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }




}
