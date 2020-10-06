package com.example.showmori2.Dto;

import com.example.showmori2.domain.User_info;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RegisterDTO {
    private String user_id;
    private String password;
    private String user_name;
    private String user_phone;

    @Builder
    public RegisterDTO(String user_id, String password, String user_name, String user_phone) {
        this.user_id = user_id;
        this.password = password;
        this.user_name = user_name;
        this.user_phone = user_phone;
    }

    public User_info toEntity(){
        return User_info.builder()
                .user_id(user_id)//UUID.nameUUIDFromBytes(user_id.getBytes()))
                .password(password)
                .user_name(user_name)
                .user_phone(user_phone)
                .build();

    }
}
