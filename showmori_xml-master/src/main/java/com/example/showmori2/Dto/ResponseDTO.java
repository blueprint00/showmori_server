package com.example.showmori2.Dto;

import com.example.showmori2.domain.Donation_info;
import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.Reward_info;
import com.example.showmori2.domain.User_info;
import org.apache.catalina.User;

import java.util.List;

public class ResponseDTO {

    //    private Donation_info donation_info;
    private List<PostResponseDTO> post;
//    private UserResponseDTO user;

    public List<PostResponseDTO> getPost() {
        return post;
    }

//    public UserResponseDTO getUserResponseDTO() {
//        return user;
//    }

    public void setPost(List<PostResponseDTO> post) {
        this.post = post;
    }

//    public void setUserResponseDTO(UserResponseDTO user) {
//        this.user = user;
//    }
}
