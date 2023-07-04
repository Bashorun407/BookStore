package com.akinnova.bookstoredemo.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String title;
    private String comment;
    private String username;
}
