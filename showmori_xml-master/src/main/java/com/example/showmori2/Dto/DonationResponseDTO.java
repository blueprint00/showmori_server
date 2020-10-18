package com.example.showmori2.Dto;

import com.example.showmori2.domain.Donation_info;
import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.User_info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DonationResponseDTO {
    private int donation_money;
    private Date selected_date;

//    private Post_info post_info;
    private User_info user_info;

    public DonationResponseDTO(Donation_info entity){
        this.donation_money = entity.getDonation_money();
        this.selected_date = entity.getSelected_date();
//        this.post_info = entity.getPost_info();
        this.user_info = entity.getUser_info();
    }
}
