package com.example.showmori2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor // 기본 생성자
@Entity
@Getter
@Table(name="user")
public class User_info {
    @Id
    private String user_id;

    @Column(nullable = false, length = 45)
    private String password;
    @Column(nullable = false, length = 10)
    private String user_name;
    @Column(nullable = false, length = 10)
    private String user_phone;

    @Builder
    public User_info(String user_id, String password, String user_name, String user_phone) {
        this.user_id = user_id;
        this.password = password;
        this.user_name = user_name;
        this.user_phone = user_phone;
    }

   @Builder
    public User_info(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }
}
