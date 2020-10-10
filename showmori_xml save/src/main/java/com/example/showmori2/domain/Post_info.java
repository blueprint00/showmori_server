package com.example.showmori2.domain;

import jdk.nashorn.internal.runtime.RewriteException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name="post")
public class Post_info {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long post_id;
    @Column(nullable = false, length = 45)
    private String poster;
    @Column(nullable = false, length = 45)
    private String title;
    @Column(nullable = false, length = 500)
    private String contents;
    @Column(nullable = false)
    private int total_donation;
    @Column(nullable = false)
    private int goal_sum;
    @Column(nullable = false)
    private Date dead_line;
    @Column(nullable = false)
    private Date start_day;
    @Column(nullable = false)
    private Date last_day;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User_info user_info;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Reward_info> rewardList = new ArrayList<>();

    @Builder
    public Post_info(Long post_id, String poster, String title, String contents, int total_donation, int goal_sum,
                     Date dead_line, Date start_day, Date last_day, User_info user_info, List<Reward_info> rewardList) {
        this.post_id = post_id;
        this.poster = poster;
        this.title = title;
        this.contents = contents;
        this.total_donation = total_donation;
        this.goal_sum = goal_sum;
        this.dead_line = dead_line;
        this.start_day = start_day;
        this.last_day = last_day;
        this.user_info = user_info;
        this.rewardList = rewardList;
    }
}
