package com.kaj.myapp.auth.entity;

import com.kaj.myapp.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/profile")
public class ProfileController {
    @Autowired
    ProfileRepository proRepo;
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProfileList() {
        List<Profile> lists = proRepo.findProfileSortByid();
        Map<String, Object> map = new HashMap<>();
        List<List<String>> pets = new ArrayList<>();
        for(Profile profile : lists){
            List<String> pet = new ArrayList<>();
            pet.add(profile.getUser().getUserid());
            pet.add(profile.getUser().getNickname());
            pet.add(profile.getPetname());
            pet.add(profile.getSpecies());
            pets.add(pet);
            System.out.println(pet);
        }
        System.out.println(pets);
        map.put("data", pets);
        return ResponseEntity.ok().body(map);
    }

}
