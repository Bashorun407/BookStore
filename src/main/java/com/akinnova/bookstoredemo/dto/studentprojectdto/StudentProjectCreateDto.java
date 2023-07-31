package com.akinnova.bookstoredemo.dto.studentprojectdto;

import lombok.Data;

@Data
public class StudentProjectCreateDto {
    private String imageAddress;
    private String schoolName;
    private String faculty;
    private String department;
    private String projectTitle;
    private String summary;
    private Double price;
    private String serialNumber;
    private String createdBy;
    private String email;

}
