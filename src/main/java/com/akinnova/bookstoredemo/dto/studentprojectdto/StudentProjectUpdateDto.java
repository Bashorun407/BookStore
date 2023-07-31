package com.akinnova.bookstoredemo.dto.studentprojectdto;

import lombok.Data;

@Data
public class StudentProjectUpdateDto {
    private String imageAddress;
    private String schoolName;
    private String faculty;
    private String department;
    private String projectTitle;
    private String summary;
    private Double price;
    private String serialNumber;
    private String modifiedBy;
    private String email;
}
