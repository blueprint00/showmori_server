package com.example.showmori2.domain;

import com.example.showmori2.Dto.DonationResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Donation_info_repository extends JpaRepository<Donation_info, Long> {

    @Query(value = "SELECT * FROM donation WHERE post_id = ?1", nativeQuery = true)
    public List<DonationResponseDTO> getDonation_infoByPostID(@Param("post_id")Long post_id);

    @Query(value = "SELECT * FROM donation WHERE user_id = ?1", nativeQuery = true)
    public List<DonationResponseDTO> getDonation_infoByUserID(@Param("user_id")String user_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM donation WHERE post_id = ?1", nativeQuery = true)
    public void deleteDonation_infoByPostID(@Param("post_id")Long post_id);

}
