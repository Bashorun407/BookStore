package com.akinnova.bookstoredemo.dto.handoutdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HandOutResponseDto {
    private String imageAddress;
    private String courseCode;
    private String courseTitle;
    private String summary;
    private int level;
}
