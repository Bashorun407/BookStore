package com.akinnova.bookstoredemo.dto.handoutdto;

import lombok.Data;
@Data
public class HandOutCreateDto {
    private String imageAddress;
    private String schoolName;
    private String faculty;
    private String department;
    private Integer level;
    private String courseCode;
    private String courseTitle;
    private String summary;
    private String serialNumber;
    private String createdBy;
    private String email;

}
