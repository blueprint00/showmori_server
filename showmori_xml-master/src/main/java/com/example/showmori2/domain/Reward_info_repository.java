package com.example.showmori2.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Repository
public interface Reward_info_repository extends JpaRepository<Reward_info, Long> {

//    Insert postreward(post_id, reward_id)
//    Select 3, 3 from reward where reward_money = 20000;


    //상세 페이지에서 리워드 리스트 띄우기
    @Query(value = "SELECT * FROM reward WHERE post_id = ?1", nativeQuery = true)
    Stream<Reward_info> getRewardinfoByPostID(@Param("post_id")Long post_id);


    //수정 시 리워드 수정
    @Modifying
    @Transactional
    @Query(value = "UPDATE reward SET reward_money = ?2, reward = ?3 WHERE post_id = ?1", nativeQuery = true)
    void updateReward(@Param("post_id")Long post_id,
                      @Param("reward_money")int reward_money,
                      @Param("reward")String reward);

    // 공연 게시 시 리워드 게시
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reward (post_id, reward_money, reward) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void insertReward(@Param("post_id")Long post_id,
                      @Param("reward_money")int reward_money,
                      @Param("reward")String reward);

    //게시 삭제
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM reward WHERE post_id = ?1", nativeQuery = true)
    void deleteRewardByPostID(@Param("post_id")Long post_id);

}
