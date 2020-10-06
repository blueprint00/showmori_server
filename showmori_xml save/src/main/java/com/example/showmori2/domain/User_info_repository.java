package com.example.showmori2.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface User_info_repository extends JpaRepository<User_info, String> {

    @Query(value = "SELECT user_id, password, user_name, user_phone FROM user", nativeQuery = true)
    Stream<User_info> getList();

    @Query(value = "SELECT password FROM user u WHERE u.user_id = ?1", nativeQuery = true)
    String findPasswordById(@Param("user_id")String userid);

    @Query(value = "SELECT COUNT(user_id) FROM user u WHERE u.user_id = ?1", nativeQuery = true)
    Long checkRegisterId(@Param("user_id")String userid);

//    @Query(value = "SELECT COUNT(user_id) FROM user u WHERE u.user_id IN (?1)", nativeQuery = true)
//    long checkRegisterId(@Param("user_id")String user_id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void saveRegisterInfo(@Param("user_id")String user_id, @Param("password")String password,
                            @Param("user_name")String user_name, @Param("user_phone")String user_phone);
}
