package com.example.showmori2.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckDTO {

    private String user_id;
    private boolean success;
    private boolean validate;

    public CheckDTO(boolean success) {
        this.success = success;
    }

    public String getUser_id() {
        return user_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isValidate() {
        return validate;
    }
}
