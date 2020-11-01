package com.example.showmori2.Dto;

import com.example.showmori2.domain.Donation_info;
import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.User_info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DonationResponseDTO {
    private int donation_money;
    private String selected_date;

//    private Post_info post_info;
//    private User_info user_info;

    private List<UserResponseDTO> user;
    private PostResponseDTO post;

//    public DonationResponseDTO(int donation_money, String selected_date, Post_info post_info, User_info user_info, UserResponseDTO userResponseDTO){//, PostResponseDTO postResponseDTO) {
//        this.donation_money = donation_money;
//        this.selected_date = selected_date;
//        this.post_info = post_info;
//        this.user_info = user_info;
//        this.user = userResponseDTO;
//        this.postResponseDTO = postResponseDTO;
//    }

    public DonationResponseDTO(int donation_money, String selected_date, Post_info post_info, User_info user_info) {
        this.donation_money = donation_money;
        this.selected_date = selected_date;
//        this.user_info = user_info;
//        this.post_info = post_info;
    }

    public DonationResponseDTO(Donation_info entity){
        this.donation_money = entity.getDonation_money();
        this.selected_date = String.valueOf(entity.getSelected_date());
//        this.post_info = entity.getPost_info();
//        this.user_info = entity.getUser_info();
    }

}
//    Title
//    total_donation
//    goal_sum
//    dead_line
//    Donation {
//        user_id
//        donation_money
//        selected_day
//        user_phone
//    }
