package com.example.showmori2.Dto;

import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.Reward_info;
import com.example.showmori2.domain.User_info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDTO {
    private long post_id;
    private String poster;
    private String image;
    private String title;
    private String contents;
    private int goal_sum;
    private Date dead_line;
    private int total_donation;
    private List<RewardResponseDTO> reward_list;
    private List<DonationResponseDTO> donation_list;

    private User_info user_info;


    public PostResponseDTO(long post_id, String poster, String image, String title, String contents, int goal_sum, Date dead_line, int total_donation, List<RewardResponseDTO> reward_list, List<DonationResponseDTO> donation_list){//}, User_info user_info) {//
        this.post_id = post_id;
        this.poster = poster;
        this.image = image;
        this.title = title;
        this.contents = contents;
        this.goal_sum = goal_sum;
        this.dead_line = dead_line;
        this.total_donation = total_donation;
        this.reward_list = reward_list;
        this.donation_list = donation_list;
//        this.user_info = user_info;
    }

    public PostResponseDTO(Post_info entity) {
        post_id = entity.getPost_id();
        poster = entity.getPoster();
        title = entity.getTitle();
        contents = entity.getContents();
        goal_sum = entity.getGoal_sum();
        dead_line = entity.getDead_line();
        total_donation = entity.getTotal_donation();
//        reward_list = entity.getRewardList();
//        donation_list = entity.getDonationList();

        user_info = entity.getUser_info();
    }
}
