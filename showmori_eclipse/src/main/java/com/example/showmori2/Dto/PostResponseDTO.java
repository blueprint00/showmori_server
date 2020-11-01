package com.example.showmori2.Dto;

import com.example.showmori2.domain.Post_info;
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
    private Long post_id;
//    private Sring user_id
    private String poster;
    private String poster_image;
    private String title;
    private String contents;
    private String contents_image;
    private int goal_sum;
    private Date dead_line;
    private Date start_day;
    private Date last_day;
    private int total_donation;
    private List<RewardResponseDTO> reward_list;
    private List<DonationResponseDTO> donation_list;

    private User_info user_info;

    public PostResponseDTO(Long post_id, String user_id, String poster, String poster_image, String title, String contents, String contents_iamge, int goal_sum,
                           String dead_line, String start_day, String last_day, int total_donation,
                           List<RewardResponseDTO> reward_list, List<DonationResponseDTO> donation_list,
                           User_info user_info) {//
        this.post_id = post_id;
        this.poster = poster;
        this.poster_image = poster_image;
        this.title = title;
        this.contents = contents;
        this.contents_image = contents_image;
        this.goal_sum = goal_sum;
        this.dead_line = Date.valueOf(dead_line);
        this.start_day = Date.valueOf(start_day);
        this.last_day = Date.valueOf(last_day);
        this.total_donation = total_donation;
        this.reward_list = reward_list;
//        this.donation_list = donation_list;
        this.user_info = user_info;
    }

    public PostResponseDTO(Post_info entity) {
        post_id = entity.getPost_id();
        poster = entity.getPoster();
        title = entity.getTitle();
        contents = entity.getContents();
        goal_sum = entity.getGoal_sum();
        dead_line = entity.getDead_line();
        start_day = entity.getStart_day();
        last_day = entity.getLast_day();
        total_donation = entity.getTotal_donation();
//        reward_list = entity.getRewardList();
//        donation_list = entity.getDonationList();

        user_info = entity.getUser_info();
    }
}
