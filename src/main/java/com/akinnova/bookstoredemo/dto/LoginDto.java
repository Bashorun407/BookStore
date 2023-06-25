package com.akinnova.bookstoredemo.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String email;
    private String password;
}
