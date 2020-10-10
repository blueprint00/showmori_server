package com.example.showmori2.Service;

import com.example.showmori2.Dto.*;
import com.example.showmori2.domain.Post_info;
import com.example.showmori2.domain.Post_info_repository;
import com.example.showmori2.domain.Reward_info;
import com.example.showmori2.domain.Reward_info_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private Post_info_repository post_info_repository;
    @Autowired
    private Reward_info_repository reward_info_repository;

    @Transactional // 메이 페이지 == 전체 공연
    public List<PostResponseDTO> getPostInfoList(){
        return post_info_repository.getPostList()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional // 검색
    public List<PostResponseDTO> findPostInfoByTitle(String title){
        List<PostResponseDTO> postList = post_info_repository.findPostInfoByTitle(title)
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());

        List<RewardResponseDTO> rewardList = new ArrayList<>();
        List<PostResponseDTO> response = new ArrayList<>();

        for(int i = 0; i < postList.size(); i ++){
//            response.set(i, postList.getReward_list());
            rewardList = reward_info_repository.getRewardinfoByPostId(postList.get(i).getPost_id())
                                                .map(RewardResponseDTO::new)
                                                .collect(Collectors.toList());
            postList.get(i).setReward_list(rewardList);
        }

        return post_info_repository.findPostInfoByTitle(title)
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional // 공연 게시
    public void insertPost(PostDTO postDTO){
        post_info_repository.save(postDTO.toEntity());
    }

    @Transactional //검색...
    public Post_info getPostInfoByPostId(Long post_id){
        return post_info_repository.getPostInfoByPostId(post_id);
    }

    @Transactional // 게시글 찾기.... -> 상세페이지에서 고칠때
    public Post_info getPostInfoById(String user_id, String title){
        return post_info_repository.getPostInfoById(user_id, title);
    }

    @Transactional // 수정
    public void updatePost(PostDTO postDTO){
        post_info_repository.updatePost(postDTO.getPoster(), postDTO.getTitle(), postDTO.getContents(), postDTO.getGoal_sum(),
                                    postDTO.getDead_line(), postDTO.getStart_day(), postDTO.getLast_day(),
                                    postDTO.getUser_info().getUser_id());

        for(Reward_info reward_info : postDTO.getRewardList()) {
            reward_info_repository.insertReward(postDTO.getPost_id(), reward_info.getReward_money(), reward_info.getReward());
        }
    }

    @Transactional // 삭제
    public void deletePost(String title){
        post_info_repository.deletePost(title);
        long post_id = post_info_repository.getPostIdByTitle(title);
        reward_info_repository.deleteRewardByPostID(post_id);
    }

    @Transactional
    public Long getPostIdByTitle(String title){
        return post_info_repository.getPostIdByTitle(title);
    }


    @Transactional
    public List<RewardResponseDTO> getRewardinfoByPostId(Long post_id){
        return reward_info_repository.getRewardinfoByPostId(post_id)
                .map(RewardResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void insertReward(Long post_id, int reward_money, String reward){
        reward_info_repository.insertReward(post_id, reward_money, reward);
    }


}
