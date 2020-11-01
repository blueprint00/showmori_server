package com.example.showmori2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor // 기본 생성자
@Entity
@Getter
@Setter
@Table(name="user")
public class User_info {
    @Id
    @Column(name = "user_id", nullable = false)
    private String user_id;

    @Column(name = "password", nullable = false, length = 45)
    private String password;
    @Column(name = "user_name", nullable = false, length = 45)
    private String user_name;
    @Column(name = "user_phone", nullable = false, length = 45)
    private String user_phone;

    @OneToMany
    @JsonIgnore
    List<Post_info> post_list = new ArrayList<>();

    @OneToMany
    @JsonIgnore
    List<Donation_info> donation_list = new ArrayList<>();

    @Builder
    public User_info(String user_id, String password, String user_name, String user_phone, List<Post_info> post_list, List<Donation_info> donation_list) {
        this.user_id = user_id;
        this.password = password;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.post_list = post_list;
        this.donation_list = donation_list;
    }

   @Builder
    public User_info(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }
}
