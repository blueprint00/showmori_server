package com.example.showmori2.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Donation_info_repository extends JpaRepository<Donation_info, Long> {

}
