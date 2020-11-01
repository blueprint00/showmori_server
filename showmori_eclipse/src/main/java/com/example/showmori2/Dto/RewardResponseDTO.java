package com.example.showmori2.Dto;

import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.Reward_info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RewardResponseDTO {
	private int reward_money;
    private String reward;

    private Post_info post_info;

    public RewardResponseDTO(int reward_money, String reward, Post_info post_info) {
        this.reward_money = reward_money;
        this.reward = reward;
        this.post_info = post_info;
    }

    public RewardResponseDTO(Reward_info entity) {
        reward_money = entity.getReward_money();
        reward = entity.getReward();
    }
}
