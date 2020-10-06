package com.example.showmori2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name="reward")
public class Reward_info {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reward_id;
    @Column(nullable = false, length = 45)
    private String donation_reward;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private long post_id;
//
//    @JoinColumn(name = "donation_money")
//    private int donaiton_money;


}
