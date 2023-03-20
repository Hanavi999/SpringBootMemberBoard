package com.example.session.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String title;

    private String content;

    private Long count;

}
