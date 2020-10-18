package com.example.showmori2.Service;

import com.example.showmori2.Dto.DonationResponseDTO;
import com.example.showmori2.domain.Donation_info_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {

    @Autowired
    private Donation_info_repository donation_info_repository;

    @Transactional
    public List<DonationResponseDTO> getDonationListByUserId(String user_id){
        return donation_info_repository.getDonation_infoByUserID(user_id);
    }

}
