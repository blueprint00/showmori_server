package com.example.showmori2.domain;

import java.util.List;

public class RequestInfo {

//    private Donation_info donation_info;
    private Post_info post_info;
    private List<Reward_info> rewardDTOList;
    private User_info user_info;

//    public void setDonation_info(Donation_info donation_info) {
//        this.donation_info = donation_info;
//    }

    public void setPost_info(Post_info post_info) {
        this.post_info = post_info;
    }

    public void setRewardDTOList(List<Reward_info> rewardDTOList) {
        this.rewardDTOList = rewardDTOList;
    }

    public void setUser_info(User_info user_info) {
        this.user_info = user_info;
    }

//    public Donation_info getDonation_info() {
//        return donation_info;
//    }

    public Post_info getPost_info() {
        return post_info;
    }

    public List<Reward_info> getRewardDTOList() {
        return rewardDTOList;
    }

    public User_info getUser_info() {
        return user_info;
    }
}
