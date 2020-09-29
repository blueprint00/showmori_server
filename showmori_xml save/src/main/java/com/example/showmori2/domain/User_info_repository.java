package com.example.showmori2.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface User_info_repository extends JpaRepository<User_info, String> {
    @Query(value = "SELECT * FROM user u", nativeQuery = true)
    List<User_info> getList();

    @Query(value = "SELECT password FROM user u where u.user_id = :user_id", nativeQuery = true)
    String findPassword(@Param("user_id")String user_id);

    @Query(value = "SELECT COUNT(user_id) FROM user u WHERE u.user_id = :user_id", nativeQuery = true)
    Long countRegisterID(@Param("user_id")String user_id);
}
