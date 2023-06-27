package com.akinnova.bookstoredemo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDto {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String username;
    private String email;
    private String password;
}
