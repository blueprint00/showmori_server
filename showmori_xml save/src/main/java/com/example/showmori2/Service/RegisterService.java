package com.example.showmori2.Service;

import com.example.showmori2.Dto.LoginDTO;
import com.example.showmori2.Dto.LoginResponseDTO;
import com.example.showmori2.Dto.RegisterDTO;
import com.example.showmori2.Dto.RegisterResponseDTO;
import com.example.showmori2.domain.User_info_repository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class RegisterService {
    @Autowired
    private User_info_repository user_info_repository;

//    @Transactional
    public void saveUserinfo(String user_id, String password, String user_name, String user_phone){
        RegisterDTO registerDTO = RegisterDTO.builder()
                .user_id(user_id)
                .password(password)
                .user_name(user_name)
                .user_phone(user_phone)
                .build();

        user_info_repository.save(registerDTO.toEntity());
//        user_info_repository.saveRegisterInfo(user_id, password, user_name, user_phone);
        System.out.println("service");
    }

    @Transactional
    public Long checkRegisterId(String user_id){
        return user_info_repository.checkRegisterId(user_id);
    }

}
