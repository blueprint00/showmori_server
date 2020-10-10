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


    @Query(value = "SELECT * FROM reward WHERE post_id = ?1", nativeQuery = true)
    Stream<Reward_info> getRewardinfoByPostId(@Param("post_id")Long post_id);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reward (post_id, reward_money, reward) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void insertReward(@Param("post_id")Long post_id,
                      @Param("reward_money")int reward_money,
                      @Param("reward")String reward);


    @Query(value = "DELETE FROM reward WHERE post_id = ?1", nativeQuery = true)
    void deleteRewardByPostID(@Param("post_id")Long post_id);

}
