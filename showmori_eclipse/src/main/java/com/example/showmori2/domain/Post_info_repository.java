package com.example.showmori2.domain;

import com.example.showmori2.Dto.DonationResponseDTO;
import com.example.showmori2.Dto.PostResponseDTO;
import com.example.showmori2.Dto.RewardResponseDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface Post_info_repository extends JpaRepository<Post_info, Long> {

    //전체 페이지
    @Query(value = "SELECT * FROM post", nativeQuery = true)
    Stream<Post_info> getPostList();

    //타이틀 중복 확인
    @Query(value = "SELECT count(title) FROM post WHERE title = ?1", nativeQuery = true)
    Long checkTitle(String title);

    //검색 결과
    @Query(value = "SELECT * FROM post WHERE title LIKE %?1%", nativeQuery = true)
    Stream<Post_info> findPostInfoByTitle(@Param("title")String title);

    @Query(value = "SELECT * FROM post WHERE user_id = ?1" , nativeQuery = true)
    Stream<Post_info> findPostInfoByUserId(@Param("user_id")String user_id);


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
	Stream<Post_info> getPostInfoByPostID(@Param("post_id")Long post_id);
    
    //해당 게시물 등록 유저 확인
    @Query(value = "SELECT user_id FROM post WHERE post_id = ?1", nativeQuery = true)
    User_info getUserInfoByPostId(@Param("post_id")Long post_id);

    //공연 수정
    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET poster = ?2, title = ?3, contents = ?4, total_donation = ?5, goal_sum = ?6," +
            "dead_line = ?7, start_day = ?8, last_day = ?9 WHERE post_id = ?1", nativeQuery = true)
    void updatePost(@Param("post_id")Long post_id,
                    @Param("poster")String poster,
                    @Param("title")String title,
                    @Param("contents")String contents,
                    @Param("total_donation")int total_donation,
                    @Param("goal_sum")int goal_sum,
                    @Param("dead_line")Date dead_line,
                    @Param("start_day")Date start_day,
                    @Param("last_day")Date last_day);
//                    @Param("reward")List<Reward_info> reward_infoList);

    //공연 삭제
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post WHERE post_id = ?1", nativeQuery = true) // ON DELETE CASCADE
    void deletePostByPostID(@Param("post_id")Long post_id);//@Param("title")String title);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post WHERE user_id = ?1", nativeQuery = true)
    void deletePostByUserId(@Param("user_id")String user_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post WHERE title = ?1", nativeQuery = true)
    void deletePostByTitle(@Param("title")String title);

    @Query(value = "SELECT post_id FROM post WHERE title = ?1", nativeQuery = true)
    Long getPostIdByTitle(@Param("title")String title);

    


    @Query(value = "SELECT * FROM post WHERE user_id = ?1", nativeQuery = true)
    Stream<PostResponseDTO> getPostInfoByUserId(String user_id);


}
