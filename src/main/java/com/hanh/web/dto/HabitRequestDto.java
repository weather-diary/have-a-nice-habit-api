package com.hanh.web.dto;

import com.hanh.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitRequestDto {

    private User user;

    private String title;

    private int count;

    private String emoji;

    @Builder
    public HabitRequestDto(
            User user,
            int count,
            String title,
            String emoji
    ){
        this.user = user;
        this.count = count;
        this.title = title;
        this.emoji = emoji;
    }

}