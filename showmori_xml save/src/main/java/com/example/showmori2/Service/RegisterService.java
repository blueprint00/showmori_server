package com.example.showmori2.Service;

import com.example.showmori2.Dto.LoginDTO;
import com.example.showmori2.Dto.LoginResponseDTO;
import com.example.showmori2.Dto.RegisterDTO;
import com.example.showmori2.Dto.RegisterResponseDTO;
import com.example.showmori2.domain.User_info_repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RegisterService {
    private User_info_repository user_info_repository;

    @Transactional
    public void saveUserinfo(String user_id, String password, String user_name, String user_phone){
        RegisterDTO registerDTO = RegisterDTO.builder()
                .user_id(user_id)
                .password(password)
                .user_name(user_name)
                .user_phone(user_phone)
                .build();

        user_info_repository.save(registerDTO.toEntity());
        System.out.println("service");
    }
//
//    public List<LoginResponseDTO> findByUserId(String user_id){
//        return user_info_repository.getList(user_id)
//                .map(LoginResponseDTO::new)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public List<LoginResponseDTO> findPasswordById(String user_id){
//        return user_info_repository.findPassword(user_id)
//                .map(LoginResponseDTO::new)
//                .collect(Collectors.toList());
//    }
}
