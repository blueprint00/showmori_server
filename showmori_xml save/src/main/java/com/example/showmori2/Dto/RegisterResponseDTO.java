package com.example.showmori2.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class RegisterResponseDTO {
    private String user_id;
    private Boolean validate;
    private Boolean success;

    public RegisterResponseDTO(String user_id, Boolean validate, Boolean success) {
        this.user_id = user_id;
        this.validate = validate;
        this.success = success;
    }
}
