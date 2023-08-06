package com.akinnova.bookstoredemo.dto.studentprojectdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentProjectResponseDto {
    private String imageAddress;
    private String projectTitle;
    private String summary;
    private Double price;
}
