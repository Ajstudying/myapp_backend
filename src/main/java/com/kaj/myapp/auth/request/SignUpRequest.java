package com.kaj.myapp.auth.request;

import com.kaj.myapp.auth.entity.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String userId;
    private String password;
    private String nickname;
    private List<Profile> profileList;

}
