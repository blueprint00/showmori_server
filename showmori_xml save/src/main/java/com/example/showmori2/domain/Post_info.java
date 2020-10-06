package com.example.showmori2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

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
    private Date contents;
    @Column(nullable = false)
    private int goal_sum;
    @Column(nullable = false)
    private Date deadline;
    @Column(nullable = false)
    private Date start_day;
    @Column(nullable = false)
    private Date last_day;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User_info userinfo;

    @Builder
    public Post_info(Long post_id, String poster, String title, Date contents, int goal_sum, Date deadline, Date start_day, Date last_day, User_info userinfo) {
        this.post_id = post_id;
        this.poster = poster;
        this.title = title;
        this.contents = contents;
        this.goal_sum = goal_sum;
        this.deadline = deadline;
        this.start_day = start_day;
        this.last_day = last_day;
//        this.userinfo = userinfo;
    }
}
