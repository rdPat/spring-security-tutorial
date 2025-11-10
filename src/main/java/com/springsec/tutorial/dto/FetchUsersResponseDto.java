package com.springsec.tutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class FetchUsersResponseDto
{
    private String username;
    private Long userId;

    public FetchUsersResponseDto(String username,Long userId)
    {
        this.userId=userId;
        this.username=username;
    }
}
