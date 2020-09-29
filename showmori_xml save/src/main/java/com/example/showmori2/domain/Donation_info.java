package com.example.showmori2.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Date donaiton_date;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private long post_id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private String user_id;


}
