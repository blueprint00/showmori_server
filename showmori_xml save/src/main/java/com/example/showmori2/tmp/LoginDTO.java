package com.example.showmori2.tmp;

import com.example.showmori2.domain.User_info;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    private String user_id;
    private String password;

    public User_info toEntity(){
        return User_info.builder()
                    .user_id(user_id)//UUID.nameUUIDFromBytes(user_id.getBytes()))
                    .password(password)
                    .build();

    }

}
