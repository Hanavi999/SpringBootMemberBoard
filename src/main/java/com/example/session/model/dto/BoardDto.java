package com.example.session.model.dto;

import lombok.Data;

@Data
public class BoardDto {

    private Long id;

    private String username;

    private String nickname;

    private String title;

    private String content;

    private Long count;

}
