package com.example.showmori2.Dto;

import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.Reward_info;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RewardDTO {
	private int reward_money;
    private String reward;

    private Post_info post_info;

    @Builder
    public RewardDTO(int reward_money, String reward, Post_info post_info) {
        this.reward_money = reward_money;
        this.reward = reward;
        this.post_info = post_info;
    }

    public Reward_info toEntity(){
        return Reward_info.builder()
                .reward_money(reward_money)
                .reward(reward)
                .post_info(post_info)
                .build();
    }

}
