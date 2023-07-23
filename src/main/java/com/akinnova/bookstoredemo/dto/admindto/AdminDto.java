package com.akinnova.bookstoredemo.dto.admindto;

import lombok.Data;

@Data
public class AdminDto {
    private String imageAddress;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String contactNumber;
    private String role;

}
