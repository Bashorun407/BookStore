package com.akinnova.bookstoredemo.dto.handoutdto;

import lombok.Data;

@Data
public class HandOutSearchDto {
    private String schoolName;
    private String courseCode;
    private String courseTitle;
    private int level;
}
