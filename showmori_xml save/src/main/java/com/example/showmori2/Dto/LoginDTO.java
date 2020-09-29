package com.example.showmori2.Dto;

import com.example.showmori2.domain.User_info;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    private String user_id;
    private String password;

    public User_info toEntity(){
        return User_info.builder()
                    .user_id(user_id)
                    .password(password)
                    .build();

    }

}
