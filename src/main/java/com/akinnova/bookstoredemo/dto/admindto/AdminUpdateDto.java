package com.akinnova.bookstoredemo.dto.admindto;

import lombok.Data;

@Data

public class AdminUpdateDto {
    private String imageAddress;
    private String username;
    private String email;
    private String password;
    private String contactNumber;
    private String role;
    private boolean activeStatus;
}
