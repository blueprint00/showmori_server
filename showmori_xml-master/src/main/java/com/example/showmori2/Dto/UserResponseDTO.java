package com.example.showmori2.Dto;

import com.example.showmori2.domain.User_info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private String user_id;
    private String password;
    private String user_name;
    private String user_phone;

    private Boolean validate;
    private Boolean success;

    public UserResponseDTO(String user_id, String password, String user_name, String user_phone) {
        this.user_id = user_id;
        this.password = password;
        this.user_name = user_name;
        this.user_phone = user_phone;
    }

    public UserResponseDTO(String user_id, Boolean validate, Boolean success) {
        this.user_id = user_id;
        this.validate = validate;
        this.success = success;
    }

    public UserResponseDTO(User_info entity){
        user_id = entity.getUser_id();
        password = entity.getPassword();
        user_name = entity.getUser_name();
        user_phone = entity.getUser_phone();
    }
}
