package com.akinnova.bookstoredemo.dto.likedto;

import lombok.Data;

@Data
public class LikeDto {
    private String username;
    private String title;
    private Boolean likes;
    //private String likeSpecNum;
}
