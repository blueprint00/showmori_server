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
public class PostDTO {
    private long post_id;
    private String poster;
    private String title;
    private String contents;
    private int total_donation;
    private int goal_sum;
    private Date dead_line;
    private Date start_day;
    private Date last_day;
    private List<Reward_info> rewardList;

    private User_info user_info;

    public Post_info toEntity(){
        return Post_info.builder()
                .post_id(post_id)
                .title(title)
                .contents(contents)
                .total_donation(total_donation)
                .goal_sum(goal_sum)
                .dead_line(dead_line)
                .start_day(start_day)
                .last_day(last_day)
                .user_info(user_info)
//                .rewardList(rewardList)
                .user_info(user_info)
                .build();
    }
}
