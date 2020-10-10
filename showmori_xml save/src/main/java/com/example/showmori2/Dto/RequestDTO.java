package com.example.showmori2.Dto;

import com.example.showmori2.domain.Donation_info;
import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.Reward_info;
import com.example.showmori2.domain.User_info;

import java.util.List;

public class RequestDTO {

    //    private Donation_info donation_info;
    private PostDTO post;
    private List<RewardDTO> rewardList;
    private UserDTO user;

    public PostDTO getPostDTO() {
        return post;
    }

    public List<RewardDTO> getRewardTOList() {
        return rewardList;
    }

    public UserDTO getUserDTO() {
        return user;
    }

    public void setPostDTO(PostDTO post) {
        this.post = post;
    }

    public void setRewardDTOList(List<RewardDTO> rewardList) {
        this.rewardList = rewardList;
    }

    public void setUserDTO(UserDTO user) {
        this.user = user;
    }
}