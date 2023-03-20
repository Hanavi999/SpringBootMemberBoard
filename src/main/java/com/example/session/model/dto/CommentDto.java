package com.example.session.model.dto;

import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    private String username;

    private String nickname;

    private String comment;

    private Long boardId;

}
