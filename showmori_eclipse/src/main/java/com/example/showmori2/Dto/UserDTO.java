package com.example.showmori2.Dto;


import com.example.showmori2.domain.Donation_info;
import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.User_info;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String user_id;
    private String password;
    private String user_name;
    private String user_phone;

    private List<Post_info> post_list;
    private List<Donation_info> donation_list;


    @Builder
    public UserDTO(String user_id, String password, String user_name, String user_phone) {
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

