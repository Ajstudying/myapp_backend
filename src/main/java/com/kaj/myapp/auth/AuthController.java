package com.kaj.myapp.auth;

import com.kaj.myapp.auth.entity.Profile;
import com.kaj.myapp.auth.repository.ProfileRepository;
import com.kaj.myapp.auth.entity.User;
import com.kaj.myapp.auth.repository.UserRepository;
import com.kaj.myapp.auth.request.SignUpRequest;
import com.kaj.myapp.auth.util.HashUtil;
import com.kaj.myapp.auth.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Tag(name="로그인 관리 처리 API")
@RestController
@RequestMapping(value = "/api/auth", produces="text/plain;charset=UTF-8")
public class AuthController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProfileRepository profileRepo;
    @Autowired
    private AuthService service;
    @Autowired
    private HashUtil hash;
    @Autowired
    private JwtUtil jwt;

    @Value("${app.cookie.domain}")
    private String cookieDomain;
    @Value("${app.login.url}")
    private String loginUrl;
    @Value("${app.home.url}")
    private String homeUrl;

    @Operation(summary = "회원의 정보 추가(회원가입)")
    @PostMapping(value = "/signup")
    public ResponseEntity signUp(@RequestBody SignUpRequest req){
        System.out.println(req);

        if(req.getUserId() == null || req.getUserId().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getPassword() == null || req.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getNickname() == null || req.getNickname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<Profile> lists = req.getProfileList();
        System.out.println(lists);
        for(int i = 0; i < lists.size(); i++){
            if(lists.get(i).getPetname() == null || lists.get(i).getPetname().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if(lists.get(i).getSpecies() == null || lists.get(i).getSpecies().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        service.createIdentity(req);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "회원의 정보 조회 및 인증 정보 추가(로그인)")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestParam String userid, @RequestParam String password, HttpServletResponse res) {
        System.out.println(userid);
        System.out.println(password);

        Optional<User> user = userRepo.findByUserid(userid);
        if(!user.isPresent()){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder
                            .fromHttpUrl(loginUrl + "?err=Unauthorized")
                            .build().toUri())
                    .build();
        }
        boolean isVerified = hash.verifyHash(password, user.get().getSecret());

        if(!isVerified){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder
                            .fromHttpUrl(loginUrl + "?err=Unauthorized")
                            .build().toUri())
                    .build();
        }
        User u = user.get();
        Optional<List<Profile>> profile = profileRepo.findByUser_Id(u.getId());
        if(!profile.isPresent()){
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(ServletUriComponentsBuilder
                            .fromHttpUrl(loginUrl + "?err=Conflict")
                            .build().toUri())
                    .build();
        }
        String token = jwt.createToken(u.getId(), u.getUserid(), u.getNickname());
        System.out.println(token);

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge((int)(jwt.TOKEN_TIMEOUT/1000));
        cookie.setDomain(cookieDomain);

        res.addCookie(cookie);

//        return ResponseEntity
//                .status(302)
//                .location(ServletUriComponentsBuilder
//                        .fromHttpUrl("http://localhost:5500")
//                        .build().toUri())
//                .build();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(ServletUriComponentsBuilder
                        .fromHttpUrl(homeUrl)
                        .build().toUri())
                .build();

    }
    @Operation(summary = "회원의 닉네임 정보 조회", security = { @SecurityRequirement(name = "bearer-key") })
    @Auth
    @GetMapping(value = "/userinfo")
    public String getUserNickname(@RequestAttribute AuthUser authUser){
        return authUser.getNickname();
    }

//    @DeleteMapping("/logout")
//    public ResponseEntity<String> logout(@RequestParam("token") String token, HttpServletResponse res) {
//        // 토큰을 무효화시키는 로직 수행
//        // 예를 들어, 토큰을 블랙리스트에 추가하거나 DB에서 삭제
//
//        Cookie cookie = new Cookie("token", token);
//        cookie.setPath("/");
//        cookie.setMaxAge((int)(jwt.TOKEN_TIMEOUT/1000));
//        cookie.setDomain("localhost");
//
//        res.addCookie(cookie);
//
//        return ResponseEntity.ok("Logged out successfully");
//    }
}