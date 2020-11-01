package com.example.showmori2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name="reward")
public class Reward_info {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reward_id;
    @Column(nullable = false)
    private int reward_money;
    @Column(nullable = false, length = 45)
    private String reward;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post_info post_info;

    @Builder
    public Reward_info(long reward_id, int reward_money, String reward, Post_info post_info) {
        this.reward_id = reward_id;
        this.reward_money = reward_money;
        this.reward = reward;
        this.post_info = post_info;
    }
}
