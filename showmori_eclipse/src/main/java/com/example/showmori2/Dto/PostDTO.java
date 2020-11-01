package com.example.showmori2.Dto;

import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.User_info;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private long post_id;
    private String poster;// 포스터 제목
    private String poster_image; // 이미지 바이트 스트링
    private String title;
    private String contents;
    private String contents_image;
    private int total_donation;
    private int goal_sum;
    private String dead_line;
    private String start_day;
    private String last_day;
    private List<RewardDTO> reward_list;
    private List<DonationDTO> donation_list;
    
    private User_info user_info;


    @Builder
    public PostDTO(String poster, String poster_image, String title, String contents, String contents_image, int total_donation, int goal_sum, String dead_line, String start_day, String last_day, User_info user_info) {
        this.poster = poster;
        this.poster_image = poster_image;
        this.title = title;
        this.contents = contents;
        this.contents_image = contents_image;
        this.total_donation = total_donation;
        this.goal_sum = goal_sum;
//        this.dead_line = dead_line;
//        this.start_day = start_day;
//        this.last_day = last_day;
        this.user_info = user_info;
    }

    public Post_info toEntity(){
        return Post_info.builder()
                .post_id(post_id)
                .title(title)
                .contents(contents)
                .total_donation(total_donation)
                .goal_sum(goal_sum)
//                .dead_line(dead_line)
//                .start_day(start_day)
//                .last_day(last_day)
//                .user_info(user_info)
//                .reward_list(reward_list)
                .build();
    }
}
