package com.kaj.myapp.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String petname;
    private String species;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

//    public String getUserNickname() {
//        return user.getNickname();
//    }

}
