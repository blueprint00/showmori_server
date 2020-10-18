package com.example.showmori2.Service;

import com.example.showmori2.domain.User_info;
import com.example.showmori2.domain.User_info_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private User_info_repository user_info_repository;

//    @Transactional(readOnly = true)
//    public List<LoginResponseDTO> getList(){
//        System.out.println("loginService get List " + user_info_repository.findUserInfoById()
//                .map(LoginResponseDTO::new)
//                .collect(Collectors.toList()).size());
//        return user_info_repository.findUserInfoById()
//                .map(LoginResponseDTO::new)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public String findPasswordById(String user_id){
        System.out.println("loginService findPassword " + user_info_repository.findPasswordById(user_id));
        return user_info_repository.findPasswordById(user_id);
    }

    @Transactional
    public void saveUserinfo(String user_id, String password, String user_name, String user_phone){
//        UserDTO userDTO = UserDTO.builder()
//                .user_id(user_id)
//                .password(password)
//                .user_name(user_name)
//                .user_phone(user_phone)
//                .build();
//        user_info_repository.save(userDTO.toEntity());
        user_info_repository.saveRegisterInfo(user_id, password, user_name, user_phone);
    }

    @Transactional
    public void updateUserInfo(String user_id, String user_name, String user_phone){
//        UserDTO userDTO = UserDTO.builder()
//                .user_id(user_id)
//                .password(password)
//                .user_name(user_name)
//                .user_phone(user_phone)
//                .build();

        user_info_repository.updateUserInfo(user_name, user_phone, user_id);
    }

    @Transactional
    public Boolean checkRegisterId(String user_id){
        return user_info_repository.checkRegisterId(user_id);
    }

    @Transactional
    public User_info findUserInfoById(String user_id){
        return user_info_repository.findUserInfoById(user_id);
    }

    @Transactional
    public void deleteUserInfo(String user_id){
        user_info_repository.deleteUserInfo(user_id);
    }
}
