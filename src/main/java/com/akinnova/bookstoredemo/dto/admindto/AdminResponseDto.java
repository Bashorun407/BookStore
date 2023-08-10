package com.akinnova.bookstoredemo.dto.admindto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminResponseDto {
    private String imageAddress;
    private String firstName;
    private String lastName;
    private String role;

}
