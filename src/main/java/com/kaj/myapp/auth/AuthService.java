package com.kaj.myapp.auth;

import com.kaj.myapp.auth.entity.Profile;
import com.kaj.myapp.auth.entity.ProfileRepository;
import com.kaj.myapp.auth.entity.User;
import com.kaj.myapp.auth.entity.UserRepository;
import com.kaj.myapp.auth.util.HashUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private UserRepository userRepo;
    private ProfileRepository profileRepo;

    @Autowired
    private HashUtil hash;

    @Autowired
    public AuthService(UserRepository userRepo, ProfileRepository profileRepo){
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
    }

    @Transactional
    public void createIdentity(SignUpRequest req){
        User toSaveUser = User.builder()
                .userid(req.getUserId())
                .secret(hash.createHash(req.getPassword()))
                .nickname(req.getNickname())
                .build();

        User saveUser = userRepo.save(toSaveUser);

        List<Profile> lists = req.getProfilelist();
        for(int i = 0; i < lists.size(); i++) {
            Profile toSaveProfile = Profile.builder()
                    .petname(lists.get(i).getPetname())
                    .species(lists.get(i).getSpecies())
                    .user(saveUser)
                    .build();
            profileRepo.save(toSaveProfile);

        }



    }


}