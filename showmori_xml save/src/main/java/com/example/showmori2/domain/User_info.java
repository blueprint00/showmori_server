package com.example.showmori2.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor // 기본 생성자
@Entity
@Getter
@Setter
@Table(name="user")
public class User_info {
    @Id //@GeneratedValue(generator="system-uuid")
    //x@GenericGenerator(name="system-uuid", strategy = "uuid")
//    @Type(type="uuid-char")
    @Column(name = "user_id", nullable = false)
    private String user_id;

    @Column(name = "password", nullable = false, length = 45)
    private String password;
    @Column(name = "user_name", nullable = false, length = 45)
    private String user_name;
    @Column(name = "user_phone", nullable = false, length = 45)
    private String user_phone;

    @Builder
    public User_info(String user_id, String password, String user_name, String user_phone) {
        this.user_id = user_id;//UUID.nameUUIDFromBytes(user_id.getBytes())//UUID.fromString(user_id);
        this.password = password;
        this.user_name = user_name;
        this.user_phone = user_phone;
    }

   @Builder
    public User_info(String user_id, String password) {
        this.user_id = user_id;//UUID.fromString(user_id);
        this.password = password;
    }
}
