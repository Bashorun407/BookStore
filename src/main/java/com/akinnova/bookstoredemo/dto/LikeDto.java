package com.akinnova.bookstoredemo.dto;

import lombok.Data;

@Data
public class LikeDto {
    private String username;
    private String title;
    private Integer likes;
}
