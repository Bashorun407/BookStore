package com.akinnova.bookstoredemo.dto;

import lombok.Data;


@Data
public class CustomerDto {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String username;
    private String email;
    private String password;
}
