package com.akinnova.bookstoredemo.dto.customerdto;

import lombok.Data;


@Data
public class CustomerDto {
    private String imageAddress;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String username;
    private String email;
    private String password;
    private String role;
}
