package com.example.showmori2.Service;

import com.example.showmori2.Dto.UserResponseDTO;
import com.example.showmori2.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private User_info_repository user_info_repository;
//    @Autowired
//    private Post_info_repository post_info_repository;
//    @Autowired
//    private Reward_info_repository reward_info_repository;
//    @Autowired
//    private Donation_info_repository donation_info_repository;

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
        user_info_repository.saveRegisterInfo(user_id, password, user_name, user_phone);
    }

    @Transactional
    public void updateUserInfo(String user_id, String user_name, String user_phone){
        user_info_repository.updateUserInfo(user_name, user_phone, user_id);
    }

    @Transactional
    public boolean checkId(String user_id){
    	if(user_info_repository.checkId(user_id) == null) return true;
    	return false;
    	
    }

    @Transactional
    public User_info findMyInfoById(String user_id) {
        return user_info_repository.findUserInfoById(user_id);
    }

    @Transactional
    public void deleteUserInfo(String user_id){
//        post_info_repository.deletePostByUserId(user_id);
//        reward_info_repository.deleteRewardByPostID(post_info_repository.findPostInfoByUserId(user_id));
//        donation_info_repository.deleteDonation_infoByUserId(user_id);
        user_info_repository.deleteUserInfo(user_id);
    }

    @Transactional
    public List<UserResponseDTO> getUserPhoneByUserIdFromDonation(Long post_id, int donation_money){
    	return user_info_repository.getUserPhoneByUserIdFromDonation(post_id, donation_money)
    			.map(UserResponseDTO::new)
    			.collect(Collectors.toList());
    }

}
