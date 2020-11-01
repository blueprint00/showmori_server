package com.example.showmori2.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Repository
public interface User_info_repository extends JpaRepository<User_info, String> {

//    @Query(value = "SELECT user_id, password, user_name, user_phone FROM user", nativeQuery = true)
//    Stream<User_info> getList();

    @Query(value = "SELECT password FROM user WHERE user_id = ?1", nativeQuery = true)
    String findPasswordById(@Param("user_id")String userid);

    @Query(value = "SELECT user_id FROM user WHERE user_id = ?1", nativeQuery = true)
    String checkId(@Param("user_id")String userid);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void saveRegisterInfo(@Param("user_id")String user_id, @Param("password")String password,
                            @Param("user_name")String user_name, @Param("user_phone")String user_phone);

    @Query(value = "SELECT user_id, password, user_name, user_phone FROM user WHERE user_id = ?1", nativeQuery = true)
    User_info findUserInfoById(@Param("user_id")String user_id);

    @Modifying
    @Transactional
    @Query(value = //"UPDATE user u SET u.user_name = ?1, u.user_phone = ?2 WHERE u.user_id = ?3",
             "UPDATE user SET user_name = ?1, user_phone = ?2 WHERE user_id= ?3"
             , nativeQuery = true)
    void updateUserInfo(@Param("user_name")String user_name, @Param("user_phone")String user_phone,
                            @Param("user_id")String user_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user WHERE user_id = ?1", nativeQuery = true)
    void deleteUserInfo(@Param("user_id")String user_id);


//    @Query(value = "SELECT user_id, user_phone FROM user WHERE user_id in (SELECT user_id FROM donation WHERE post_id = ?1)", nativeQuery = true)
    @Query(value = "select * from user where user_id in " +
            "(select user_id from donation where post_id = ?1 and donation_money = ?2)" //in " +
//            "(select post_id from post where user_id = ?1))"
            , nativeQuery = true)
    Stream<User_info> getUserPhoneByUserIdFromDonation(@Param("post_id") Long post_id,
    													@Param("donation_money")int donation_money);

}
