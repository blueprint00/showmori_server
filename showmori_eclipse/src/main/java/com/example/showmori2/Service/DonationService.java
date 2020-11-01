package com.example.showmori2.Service;

import com.example.showmori2.Dto.DonationDTO;
import com.example.showmori2.Dto.DonationResponseDTO;
import com.example.showmori2.domain.Donation_info_repository;
import com.example.showmori2.domain.Post_info_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {

    @Autowired
    private Donation_info_repository donation_info_repository;
    @Autowired
    private Post_info_repository post_info_repository;

    @Transactional //게시글 올린 사람이 누가 후원했는지 확인하는
    public List<DonationResponseDTO> getDonation_infoByPostID(Long post_id) {
        return donation_info_repository.getDonation_infoByPostID(post_id)
                .map(DonationResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional // 한 유저가 후원한 리스트
    public List<DonationResponseDTO> getDonation_infoByUserID(String user_id) {	
        return donation_info_repository.getDonation_infoByUserID(user_id)
        		.map(DonationResponseDTO::new)
        		.collect(Collectors.toList());
    }
    
    @Transactional // 한 포스트에 후원된 리스트 >> 공연상세정보?
    public List<DonationResponseDTO> getDonation_infoByPostID(Long post_id, String user_id){
        return donation_info_repository.getDonation_infoByPostIDAndUserID(post_id, user_id)
                .map(DonationResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 게시한 사람이 누가 얼마나 후원했나 확인하는 리스트
    @Transactional
    public List<DonationResponseDTO> getDonation_infoByUserIDFromPost(String user_id){
    	return donation_info_repository.getDonation_infoByUserIDFromPost(user_id)
                .map(DonationResponseDTO::new)
                .collect(Collectors.toList());
    }



    @Transactional // 한 포스트에 후원하기
    public void insertDonationInfo(DonationDTO donationDTO, Long post_id){
        donation_info_repository.insertDonationInfo(donationDTO.getDonation_money(),
                                                    donationDTO.getSelected_date(),
                                                    donationDTO.getUser_info().getUser_id(),
                                                    post_id);
    }

    @Transactional // 후원 수정
    public void updateDonation(DonationDTO donationDTO) {
        donation_info_repository.updateDonation(donationDTO.getDonation_money(),
                                                donationDTO.getSelected_date(),
                                                donationDTO.getUser_info().getUser_id());
    }

    @Transactional // 후원 삭제
    public void deleteDonation_infoByUserIdAndTitle(String user_id, String title) {
        donation_info_repository.deleteDonation_infoByUserIdAndTitle(user_id, title);

    }
    
    @Transactional
    public boolean checkUserIdAndTitle(String user_id, String title) {
    	if(donation_info_repository.checkUserIdAndTitle(user_id, title) == null) return true;
    	return false;
    }

	public List<Long> getPostIdByUserId(String user_id) {
		return donation_info_repository.getPostIdByUserId(user_id);
	}


}
