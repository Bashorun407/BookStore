package com.akinnova.bookstoredemo.dto.handoutdto;

import lombok.Data;

@Data
public class HandOutUpdateDto {
    private String imageAddress;
    private String schoolName;
    private String faculty;
    private String department;
    private int level;
    private String courseCode;
    private String courseTitle;
    private String summary;
    private String modifiedBy;
    private String modifierEmail;
}
