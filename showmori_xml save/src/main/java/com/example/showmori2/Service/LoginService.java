package com.example.showmori2.Service;

import com.example.showmori2.Dto.LoginResponseDTO;
import com.example.showmori2.domain.User_info_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {
    @Autowired
    User_info_repository user_info_repository;

    @Transactional(readOnly = true)
    public List<LoginResponseDTO> getList(){
        System.out.println("loginService get List " + user_info_repository.getList()
                .map(LoginResponseDTO::new)
                .collect(Collectors.toList()).size());
        return user_info_repository.getList()
                .map(LoginResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String findPasswordById(String user_id){
        System.out.println("loginService findPassword " + user_info_repository.findPasswordById(user_id));
        return user_info_repository.findPasswordById(user_id);
    }
}
