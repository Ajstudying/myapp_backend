package com.kaj.myapp.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kaj.myapp.auth.entity.User;
import com.kaj.myapp.auth.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class JwtUtil {
    //임의의 서명 값
    @Autowired
    UserRepository useRepo;
    public String secret = "pet-secret";

    //초/분/시간/하루/일주일
    public final long TOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 7;

    //JWT토큰 생성
    public String createToken(Long id, String nickname){
        Date now = new Date();

        Date expire = new Date(now.getTime()+TOKEN_TIMEOUT);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(id.toString())
                .withClaim("nickname", nickname)
                .withIssuedAt(now)
                .withExpiresAt(expire)
                .sign(algorithm);
    }

    public User validateToken(String token){
        System.out.println(token);
        //검증 객체
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();

        try{
            DecodedJWT decodedJWT = verifier.verify(token);
            String userId = decodedJWT.getClaim("userId").asString();
            String nickname = decodedJWT.getClaim("nickname").asString();
            User user = User.builder().userid(userId).nickname(nickname).build();
            System.out.println(user);
            return user;
//            Optional<User> profileUser = useRepo.findByUserid(userId);
//            System.out.println(profileUser);
//            if(!(profileUser.isEmpty()) && user.getProfile() == profileUser.get().getProfile()){
//                return user;
//            }
//            return null;

        }catch (JWTVerificationException e){
            //토큰 검증 오류 상황
            return null;
        }
    }

}
