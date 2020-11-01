package com.example.showmori2.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "donation")
public class Donation_info {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long donation_id;

    @Column(nullable = false)
    private int donation_money;
    @Column(nullable = false)
    private Date selected_date;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post_info post_info;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User_info user_info;


    @Builder
    public Donation_info(Long donation_id, int donation_money, Date selected_date, User_info user_info, Post_info post_info) {
        this.donation_id = donation_id;
        this.donation_money = donation_money;
        this.selected_date = selected_date;
        this.post_info = post_info;
        this.user_info = user_info;
    }
}
