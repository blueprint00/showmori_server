package com.example.showmori2.Dto;

import com.example.showmori2.domain.Donation_info;
import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.User_info;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class DonationDTO {
//    private Long donation_id;

    private int donation_money;
    private Date selected_date;

    private Post_info post_info;
    private User_info user_info;

    @Builder
    public DonationDTO(int donation_money, Date selected_date, User_info user_info) {//Post_info post_info,
        this.donation_money = donation_money;
        this.selected_date = selected_date;
//        this.post_info = post_info;
        this.user_info = user_info;
    }

    public Donation_info toEntity(){
        return Donation_info.builder()
                .donation_money(donation_money)
//                .post_info(post_info)
                .user_info(user_info)
                .build();
    }
}
