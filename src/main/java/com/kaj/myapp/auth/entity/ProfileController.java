package com.kaj.myapp.auth.entity;

import com.kaj.myapp.auth.Auth;
import com.kaj.myapp.auth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/profile")
public class ProfileController {
    @Autowired
    ProfileRepository proRepo;
    @Autowired
    UserRepository repo;
    @Auth
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProfileList(@RequestAttribute AuthUser authUser) {
        System.out.println(authUser);
        Optional<List<Profile>> lists = proRepo.findByUser_Id(authUser.getId());
        if(!lists.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<String, Object> map = new HashMap<>();
        List<List<Object>> pets = new ArrayList<>();
        for(Profile profile : lists.get()){
            List<Object> pet = new ArrayList<>();
            pet.add(profile.getUser().getUserid());
            pet.add(profile.getUser().getNickname());
            pet.add(profile.getPetname());
            pet.add(profile.getSpecies());
            pet.add(profile.getId());
            pets.add(pet);
        }
        map.put("data", pets);
        return ResponseEntity.ok().body(map);
    }
    @Auth
    @PostMapping
    public ResponseEntity addProfile(@RequestBody Profile profile, @RequestAttribute AuthUser authUser){
        System.out.println(profile);
        //사용자 찾기
        Optional<User> findedUser = repo.findById(authUser.getId());
        if(!findedUser.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = findedUser.get();
        if(profile.getPetname() == null || profile.getPetname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(profile.getSpecies() == null || profile.getSpecies().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        profile.setUser(user);
        Profile savedProfile = proRepo.save(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
    }
    @Auth
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyProfile(@PathVariable long no, @RequestBody ProfileModifyRequest profile, @RequestAttribute AuthUser authUser){
        Optional<Profile> findPro = proRepo.findById(no);
        if(!findPro.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Profile toModifyProfile = findPro.get();
        if(profile.getPetname() != null && !profile.getPetname().isEmpty()){
            toModifyProfile.setPetname(profile.getPetname());
        }
        if(profile.getSpecies() != null && !profile.getSpecies().isEmpty()){
            toModifyProfile.setSpecies(profile.getSpecies());
        }
        proRepo.save(toModifyProfile);

        return ResponseEntity.ok().build();
    }
    @Auth
    @DeleteMapping(value = "/{no}")
    public ResponseEntity deleteProfile(@PathVariable long no, @RequestAttribute AuthUser authUser) {
        Optional<Profile> findPro = proRepo.findById(no);
        if(!findPro.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        proRepo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
