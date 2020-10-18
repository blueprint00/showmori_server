package com.example.showmori2.domain;

import com.example.showmori2.Dto.PostResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface Post_info_repository extends JpaRepository<Post_info, Long> {

    //전체 페이지
    @Query(value = "SELECT * FROM post", nativeQuery = true)
    Stream<Post_info> getPostList();

    //검색 결과
    @Query(value = "SELECT * FROM post  WHERE post.title = ?1",
            //+ " and reward.post_id = (SELECT post_id FROM post WHERE post.title = ?1)",
            nativeQuery = true)
    Stream<Post_info> findPostInfoByTitle(@Param("title")String title);

    //공연 게시
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO post (poster, title, contents, total_donation, goal_sum," +
                                "dead_line, start_day, last_day, user_id) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
    void insertPost(@Param("poster")String poster,
                       @Param("title")String title,
                       @Param("contents")String contents,
                       @Param("total_donation")int total_donation,
                       @Param("goal_sum")int goal_sum,
                       @Param("dead_line")Date dead_line,
                       @Param("start_day")Date start_day,
                       @Param("last_day")Date last_day,
                       @Param("user_id")String user_id);
//                       @Param("reward")List<Reward_info> reward_infoList);

    //공연 수정하기 위한 Post_id....
//    @Query(value = "SELECT * FROM post WHERE post_id = ?1", nativeQuery = true)
//    Post_info getPostInfoById(@Param("post_id")Long post_id);//@Param("user_id")String user_id, @Param("title")String title);


    //상세 페이지 && 수정 전 전체 정보
    @Query(value = "SELECT * FROM post WHERE post_id = ?1", nativeQuery = true)
    PostResponseDTO getPostInfoByPostID(@Param("post_id")Long post_id);

    //해당 게시물 등록 유저 확인
    @Query(value = "SELECT user_id FROM post WHERE post_id = ?1", nativeQuery = true)
    User_info getUserInfoById(@Param("post_id")Long post_id);

    //공연 수정
    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET (poster = ?1, title = ?2, contents = ?3, total_donation = ?4, goal_sum = ?5," +
            "dead_line = ?6, start_day = ?7, last_day = ?8, user_id = ?9)", nativeQuery = true)
    void updatePost(@Param("poster")String poster,
                    @Param("title")String title,
                    @Param("contents")String contents,
                    @Param("total_donation")int total_donation,
                    @Param("goal_sum")int goal_sum,
                    @Param("dead_line")Date dead_line,
                    @Param("start_day")Date start_day,
                    @Param("last_day")Date last_day,
                    @Param("user_id")String user_id);
//                    @Param("reward")List<Reward_info> reward_infoList);

    //공연 삭제
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post WHERE post_id = ?1", nativeQuery = true) // ON DELETE CASCADE
    void deletePostByPostID(@Param("post_id")Long post_id);//@Param("title")String title);

    @Query(value = "SELECT post_id FROM post WHERE title = ?1", nativeQuery = true)
    Long getPostIdByTitle(@Param("title")String title);

    @Query(value = "SELECT poster FROM post WHERE post_id = ?1", nativeQuery = true)
    String getPosterByPostID(@Param("post_id")Long post_id);



}
