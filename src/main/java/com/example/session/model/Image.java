package com.example.session.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgNm;

    private String savedNm;

    private String savedPath;

    private Long boardId;

}
