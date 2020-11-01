package com.example.showmori2.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.showmori2.Dto.DonationResponseDTO;

import java.sql.Date;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface Donation_info_repository extends JpaRepository<Donation_info, Long> {

    //게시글 올린 사람이 누가 후원했는지 확인하는
    /*  post_id |
        title |
        total_donation |
        goal_sum |
        dead_line |
        donation {
            user_id |
            user_phone
            donation_money |
            selected_date |
        }*/
	
    //  user_id 가 게시한 게시물 post.post_id에 후원 한 사람 리스트
    @Query(value = "SELECT * FROM donation WHERE post_id in (SELECT post_id FROM post WHERE user_id = ?1)", nativeQuery = true)
    Stream<Donation_info> getDonation_infoByUserIDFromPost(@Param("user_id")String user_id);

    // 한 포스트에 후원한 사람들
    @Query(value = "SELECT * FROM donation WHERE post_id = ?1", nativeQuery = true)
    Stream<Donation_info> getDonation_infoByPostID(@Param("post_id")Long post_id);
    
    @Query(value = "SELECT * FROM donation WHERE post_id = ?1 and user_id = ?2", nativeQuery = true)
    Stream<Donation_info> getDonation_infoByPostIDAndUserID(@Param("post_id")Long post_id,
    												@Param("user_id")String user_id);

    //한 사람이 후원한 리스트
    @Query(value = "SELECT * FROM donation WHERE user_id = ?1", nativeQuery = true)
    Stream<Donation_info> getDonation_infoByUserID(@Param("user_id")String user_id);

//    @Query(value = "SELECT * FROM donation WHERE user_id = ?1 AND post_id = ?2", nativeQuery = true)
//    public Stream<Donation_info> getDonationListByPostIDAndUserID(@Param("user_id")String user_id, @Param("post_id")Long post_id);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO donation (donation_money, selected_date, user_id, post_id) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insertDonationInfo(@Param("donation_money")int donation_money,
                                   @Param("selected_day")Date selected_day,
                                   @Param("user_id")String user_id,
                                   @Param("post_id")Long post_id);

    @Query(value = "UPDATE donation SET (donation_money = ?1, selected_day = ?2) where user_id = ?3", nativeQuery = true)
    public void updateDonation(@Param("donation_money")int donation_money,
                               @Param("selected_day")Date selected_day,
                               @Param("user_id")String user_id);

    //한 포스트에 해당 유저가 후원한 기록 삭제
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM donation WHERE user_id = ?1 AND post_id in (SELECT post_id FROM post WHERE title = ?2)", nativeQuery = true)
    public void deleteDonation_infoByUserIdAndTitle(@Param("user_id")String user_id, @Param("title")String title);

    //user_id && title로 지우려고 확인 
  	@Query(value = "SELECT post_id FROM donation WHERE user_id = ?1 AND post_id = (SELECT post_id FROM post WHERE title = ?2)", nativeQuery = true)
  	String checkUserIdAndTitle(@Param("user_id")String user_id, @Param("title")String title);
  	
    
    @Query(value = "SELECT post_id FROM donation WHERE user_id = ?1", nativeQuery = true)
	List<Long> getPostIdByUserId(String user_id);


}
