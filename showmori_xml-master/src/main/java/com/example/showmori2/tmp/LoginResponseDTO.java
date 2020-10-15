package com.example.showmori2.tmp;

import com.example.showmori2.domain.User_info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {
    private String user_id;
    private Boolean success;

    public LoginResponseDTO(String user_id, Boolean success) {
        this.user_id = user_id;
        this.success = success;
    }

//    public LoginResponseDTO(User_info entity) {
//        this.user_id = entity.getUser_id().toString();
//    }
}
