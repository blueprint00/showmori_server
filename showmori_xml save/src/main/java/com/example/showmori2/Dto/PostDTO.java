package com.example.showmori2.Dto;

import com.example.showmori2.domain.Post_info;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private long post_id;
    private String title;

    public Post_info toEntity(){
        return Post_info.builder()
                .post_id(post_id)
                .title(title)
                .build();
    }
}
