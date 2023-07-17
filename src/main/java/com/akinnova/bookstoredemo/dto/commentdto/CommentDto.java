package com.akinnova.bookstoredemo.dto.commentdto;

import lombok.Data;

@Data
public class CommentDto {
    private String title;
    private String comment;
    private String username;
}
