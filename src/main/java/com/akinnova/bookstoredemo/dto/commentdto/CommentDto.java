package com.akinnova.bookstoredemo.dto.commentdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    private String title;
    private String comment;
    private String username;
}
